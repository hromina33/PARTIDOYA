package com.partidoya.platform.courts.application.internal.queryservices;

import com.partidoya.platform.courts.application.queryservices.CourtQueryService;
import com.partidoya.platform.courts.application.queryservices.ManagedReservationView;
import com.partidoya.platform.courts.domain.model.aggregates.Court;
import com.partidoya.platform.courts.domain.model.queries.GetCourtByIdQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedCourtsQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedReservationsQuery;
import com.partidoya.platform.courts.domain.model.queries.SearchPublishedCourtsQuery;
import com.partidoya.platform.courts.domain.repositories.CourtRepository;
import com.partidoya.platform.courts.domain.repositories.ReservationRepository;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.iam.domain.services.PlanPolicy;
import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CourtQueryServiceImpl implements CourtQueryService {
    private final CourtRepository courtRepository;
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;

    public CourtQueryServiceImpl(CourtRepository courtRepository, ReservationRepository reservationRepository,
                                 UserRepository userRepository) {
        this.courtRepository = courtRepository;
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Court> handle(SearchPublishedCourtsQuery query) {
        return courtRepository.findPublished(query.sport(), query.district()).stream()
                .filter(court -> userRepository.findById(court.getOwnerId())
                        .map(PlanPolicy::canManageCourts)
                        .orElse(false))
                .toList();
    }

    @Override
    public Optional<Court> handle(GetCourtByIdQuery query) {
        return courtRepository.findById(query.courtId())
                .filter(Court::canReceiveReservations)
                .filter(court -> userRepository.findById(court.getOwnerId())
                        .map(PlanPolicy::canManageCourts)
                        .orElse(false));
    }

    @Override
    public List<Court> handle(GetManagedCourtsQuery query) {
        return courtRepository.findByOwnerId(query.ownerId());
    }

    @Override
    public List<ManagedReservationView> handle(GetManagedReservationsQuery query) {
        var owner = userRepository.findById(query.ownerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", query.ownerId().value().toString()));
        if (!PlanPolicy.canManageCourts(owner)) {
            throw new ForbiddenActionException("Only court administrators can view reservations");
        }

        var courts = courtRepository.findByOwnerId(query.ownerId());
        if (query.courtId() != null) {
            var ownsCourt = courts.stream().anyMatch(court -> court.getId().value().equals(query.courtId().value()));
            if (!ownsCourt) throw new ForbiddenActionException("You cannot view reservations for this court");
            courts = courts.stream().filter(court -> court.getId().value().equals(query.courtId().value())).toList();
        }
        if (courts.isEmpty()) return List.of();

        var from = query.from() == null ? LocalDate.now().minusMonths(1) : query.from();
        var to = query.to() == null ? LocalDate.now().plusMonths(3) : query.to();
        if (from.isAfter(to)) throw new IllegalArgumentException("date range is invalid");

        Map<Long, Court> courtsById = courts.stream()
                .collect(Collectors.toMap(court -> court.getId().value(), Function.identity()));

        return reservationRepository.findByCourtIds(courts.stream().map(Court::getId).toList(), from, to)
                .stream()
                .map(reservation -> {
                    var court = courtsById.get(reservation.getCourtId().value());
                    var customerName = userRepository.findById(reservation.getUserId())
                            .map(user -> user.getFullName().value())
                            .orElse("Usuario #" + reservation.getUserId().value());
                    return new ManagedReservationView(
                            reservation.getId().value(),
                            reservation.getUserId().value(),
                            customerName,
                            reservation.getCourtId().value(),
                            court != null ? court.getName() : "Cancha #" + reservation.getCourtId().value(),
                            reservation.getDate(),
                            reservation.getStartTime(),
                            reservation.getEndTime(),
                            reservation.getPrice(),
                            reservation.getCurrency(),
                            reservation.getProviderReference(),
                            reservation.getStatus().name(),
                            reservation.getPaymentStatus().name(),
                            reservation.getCreatedAt());
                })
                .toList();
    }
}
