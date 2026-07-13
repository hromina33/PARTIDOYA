package com.partidoya.platform.courts.application.internal.commandservices;

import com.partidoya.platform.courts.application.commandservices.CourtCommandService;
import com.partidoya.platform.courts.domain.model.aggregates.Court;
import com.partidoya.platform.courts.domain.model.aggregates.CourtAvailability;
import com.partidoya.platform.courts.domain.model.aggregates.Reservation;
import com.partidoya.platform.courts.domain.model.commands.CreateCourtAvailabilityCommand;
import com.partidoya.platform.courts.domain.model.commands.CreateCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.DeleteCourtAvailabilityCommand;
import com.partidoya.platform.courts.domain.model.commands.PublishCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.ReserveCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.UpdateCourtAvailabilityCommand;
import com.partidoya.platform.courts.domain.model.commands.UpdateCourtCommand;
import com.partidoya.platform.courts.domain.model.valueobjects.CourtAvailabilityType;
import com.partidoya.platform.courts.domain.repositories.CourtAvailabilityRepository;
import com.partidoya.platform.courts.domain.repositories.CourtRepository;
import com.partidoya.platform.courts.domain.repositories.ReservationRepository;
import com.partidoya.platform.courts.domain.services.PaymentProcessor;
import com.partidoya.platform.courts.domain.services.PaymentRequest;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.iam.domain.services.PlanPolicy;
import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceConflictException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class CourtCommandServiceImpl implements CourtCommandService {
    private final CourtRepository courtRepository;
    private final CourtAvailabilityRepository availabilityRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final PaymentProcessor paymentProcessor;

    public CourtCommandServiceImpl(CourtRepository courtRepository, CourtAvailabilityRepository availabilityRepository,
                                   ReservationRepository reservationRepository,
                                   UserRepository userRepository, PaymentProcessor paymentProcessor) {
        this.courtRepository = courtRepository;
        this.availabilityRepository = availabilityRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.paymentProcessor = paymentProcessor;
    }

    @Override
    public Court handle(CreateCourtCommand command) {
        ensureCanManageCourts(command.ownerId());
        return courtRepository.save(new Court(command.ownerId(), command.name(), command.complexName(),
                command.description(), command.address(), command.district(), command.latitude(), command.longitude(),
                command.pricePerHour(), command.sports(), command.schedules(), command.mainImageUrl(),
                command.imageUrls(), command.services(), command.features()));
    }

    @Override
    public Court handle(UpdateCourtCommand command) {
        ensureCanManageCourts(command.requesterId());
        var court = courtRepository.findById(command.courtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court", command.courtId().value().toString()));
        ensureOwner(court, command.requesterId());
        court.updateDetails(command.name(), command.complexName(), command.description(), command.address(),
                command.district(), command.latitude(), command.longitude(), command.pricePerHour(), command.sports(),
                command.schedules(), command.mainImageUrl(), command.imageUrls(), command.services(), command.features());
        return courtRepository.save(court);
    }

    @Override
    public Court handle(PublishCourtCommand command) {
        ensureCanManageCourts(command.requesterId());
        var court = courtRepository.findById(command.courtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court", command.courtId().value().toString()));
        ensureOwner(court, command.requesterId());
        if (command.published()) court.publish();
        else court.unpublish();
        return courtRepository.save(court);
    }

    @Override
    public Reservation handle(ReserveCourtCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", command.userId().value().toString()));
        if (!PlanPolicy.canUsePlayerFeatures(user)) {
            throw new ForbiddenActionException("Only player accounts can reserve courts");
        }
        var court = courtRepository.findById(command.courtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court", command.courtId().value().toString()));
        var owner = userRepository.findById(court.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", court.getOwnerId().value().toString()));
        if (!PlanPolicy.canManageCourts(owner) || !court.canReceiveReservations()) {
            throw new ForbiddenActionException("Court is not available for reservations");
        }
        if (LocalDateTime.of(command.date(), command.startTime()).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot reserve past schedules");
        }
        var schedule = "%s-%s".formatted(command.startTime(), command.endTime());
        var availability = availabilityRepository.findByCourtIdAndDate(command.courtId(), command.date());
        var explicitlyAvailable = availability.stream()
                .filter(item -> item.getType() == CourtAvailabilityType.AVAILABLE)
                .anyMatch(item -> item.contains(command.date(), command.startTime(), command.endTime()));
        if (!court.supportsSchedule(schedule) && !explicitlyAvailable) {
            throw new IllegalStateException("Selected schedule is not available for this court");
        }
        if (availability.stream()
                .filter(item -> item.getType() == CourtAvailabilityType.BLOCKED)
                .anyMatch(item -> item.overlaps(command.date(), command.startTime(), command.endTime()))) {
            throw new ResourceConflictException("Availability", "schedule is blocked");
        }
        if (reservationRepository.existsOverlapping(command.courtId(), command.date(), command.startTime(), command.endTime())) {
            throw new ResourceConflictException("Reservation", "schedule already reserved");
        }
        if (reservationRepository.existsByPaymentIdempotencyKey(command.idempotencyKey())) {
            throw new ResourceConflictException("Payment", "payment request already processed");
        }
        ensurePaymentContextMatches(command);
        var reservation = reservationRepository.save(new Reservation(command.userId(), command.courtId(),
                command.date(), command.startTime(), command.endTime(), court.getPricePerHour()));
        var payment = paymentProcessor.process(new PaymentRequest(reservation.getId().value(),
                reservation.getPrice(), reservation.getCurrency(), command.culqiToken(), command.payerEmail(),
                command.idempotencyKey(), "Reserva de cancha %s".formatted(court.getName())));
        if (!payment.approved()) {
            throw new IllegalStateException("Pago rechazado por Culqi.");
        }
        if (reservationRepository.existsByProviderReference(payment.providerReference())) {
            throw new ResourceConflictException("Payment", "provider operation already registered");
        }
        reservation.approvePayment(payment.providerReference(), command.idempotencyKey());
        return reservationRepository.save(reservation);
    }

    @Override
    public CourtAvailability handle(CreateCourtAvailabilityCommand command) {
        ensureCanManageCourts(command.requesterId());
        var court = courtRepository.findById(command.courtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court", command.courtId().value().toString()));
        ensureOwner(court, command.requesterId());
        var availability = new CourtAvailability(command.courtId(), command.date(), command.allDay(),
                command.startTime(), command.endTime(), command.type(), command.reason());
        ensureAvailabilityDoesNotOverlap(availability, null);
        return availabilityRepository.save(availability);
    }

    @Override
    public CourtAvailability handle(UpdateCourtAvailabilityCommand command) {
        ensureCanManageCourts(command.requesterId());
        var availability = availabilityRepository.findById(command.availabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("CourtAvailability", command.availabilityId().value().toString()));
        var court = courtRepository.findById(command.courtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court", command.courtId().value().toString()));
        ensureOwner(court, command.requesterId());
        if (!availability.getCourtId().value().equals(command.courtId().value())) {
            throw new IllegalArgumentException("Availability cannot be moved to another court");
        }
        availability.update(command.date(), command.allDay(), command.startTime(), command.endTime(),
                command.type(), command.reason());
        ensureAvailabilityDoesNotOverlap(availability, command.availabilityId());
        return availabilityRepository.save(availability);
    }

    @Override
    public void handle(DeleteCourtAvailabilityCommand command) {
        ensureCanManageCourts(command.requesterId());
        var availability = availabilityRepository.findById(command.availabilityId())
                .orElseThrow(() -> new ResourceNotFoundException("CourtAvailability", command.availabilityId().value().toString()));
        var court = courtRepository.findById(availability.getCourtId())
                .orElseThrow(() -> new ResourceNotFoundException("Court", availability.getCourtId().value().toString()));
        ensureOwner(court, command.requesterId());
        availabilityRepository.delete(availability);
    }

    private void ensureCanManageCourts(UserId ownerId) {
        var owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", ownerId.value().toString()));
        if (!PlanPolicy.canManageCourts(owner)) {
            throw new ForbiddenActionException("Only court plan accounts can manage courts");
        }
    }

    private void ensureOwner(Court court, UserId requesterId) {
        if (!court.getOwnerId().value().equals(requesterId.value())) {
            throw new ForbiddenActionException("Only the court owner can modify this court");
        }
    }

    private void ensureAvailabilityDoesNotOverlap(CourtAvailability availability,
                                                  com.partidoya.platform.courts.domain.model.valueobjects.CourtAvailabilityId excludedId) {
        if (availabilityRepository.existsOverlapping(availability.getCourtId(), availability.getDate(),
                availability.getStartTime(), availability.getEndTime(), excludedId)) {
            throw new ResourceConflictException("CourtAvailability", "schedule overlaps an existing record");
        }
    }

    private static void ensurePaymentContextMatches(ReserveCourtCommand command) {
        var key = command.idempotencyKey();
        var expectedPrefix = "reservation-%d-%d-%s-%s-".formatted(
                command.courtId().value(),
                command.userId().value(),
                command.date(),
                command.startTime());
        if (key == null || !key.startsWith(expectedPrefix)) {
            throw new IllegalArgumentException("Payment context does not match the selected reservation");
        }
    }
}
