package com.partidoya.platform.courts.domain.repositories;

import com.partidoya.platform.courts.domain.model.aggregates.Reservation;
import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.courts.domain.model.valueobjects.ReservationId;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    Optional<Reservation> findById(ReservationId id);
    boolean existsOverlapping(CourtId courtId, LocalDate date, LocalTime startTime, LocalTime endTime);
    boolean existsByPaymentIdempotencyKey(String paymentIdempotencyKey);
    boolean existsByProviderReference(String providerReference);
    List<Reservation> findByCourtId(CourtId courtId);
    List<Reservation> findByCourtIdAndDate(CourtId courtId, LocalDate date);
    List<Reservation> findByCourtIds(Collection<CourtId> courtIds, LocalDate from, LocalDate to);
    long countByUserId(com.partidoya.platform.iam.domain.model.valueobjects.UserId userId);
    Reservation save(Reservation reservation);
}
