package com.partidoya.platform.courts.infrastructure.persistence.jpa.adapters;

import com.partidoya.platform.courts.domain.model.aggregates.Reservation;
import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.courts.domain.model.valueobjects.ReservationId;
import com.partidoya.platform.courts.domain.repositories.ReservationRepository;
import com.partidoya.platform.courts.infrastructure.persistence.jpa.assemblers.ReservationPersistenceAssembler;
import com.partidoya.platform.courts.infrastructure.persistence.jpa.repositories.ReservationPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepositoryImpl implements ReservationRepository {
    private final ReservationPersistenceRepository reservationPersistenceRepository;

    public ReservationRepositoryImpl(ReservationPersistenceRepository reservationPersistenceRepository) {
        this.reservationPersistenceRepository = reservationPersistenceRepository;
    }

    @Override
    public Optional<Reservation> findById(ReservationId id) {
        return reservationPersistenceRepository.findById(id.value())
                .map(ReservationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsOverlapping(CourtId courtId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return reservationPersistenceRepository.existsByCourtIdAndDateAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
                courtId.value(), date, endTime, startTime, List.of("CONFIRMED"));
    }

    @Override
    public boolean existsByPaymentIdempotencyKey(String paymentIdempotencyKey) {
        return paymentIdempotencyKey != null && !paymentIdempotencyKey.isBlank()
                && reservationPersistenceRepository.existsByPaymentIdempotencyKey(paymentIdempotencyKey);
    }

    @Override
    public boolean existsByProviderReference(String providerReference) {
        return providerReference != null && !providerReference.isBlank()
                && reservationPersistenceRepository.existsByProviderReference(providerReference);
    }

    @Override
    public List<Reservation> findByCourtId(CourtId courtId) {
        return reservationPersistenceRepository.findByCourtId(courtId.value()).stream()
                .map(ReservationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Reservation save(Reservation reservation) {
        var entity = ReservationPersistenceAssembler.toPersistenceFromDomain(reservation);
        return ReservationPersistenceAssembler.toDomainFromPersistence(reservationPersistenceRepository.save(entity));
    }
}
