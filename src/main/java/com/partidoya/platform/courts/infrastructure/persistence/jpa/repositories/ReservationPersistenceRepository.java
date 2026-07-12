package com.partidoya.platform.courts.infrastructure.persistence.jpa.repositories;

import com.partidoya.platform.courts.infrastructure.persistence.jpa.entities.ReservationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

public interface ReservationPersistenceRepository extends JpaRepository<ReservationPersistenceEntity, Long> {
    boolean existsByCourtIdAndDateAndStartTimeLessThanAndEndTimeGreaterThanAndStatusIn(
            Long courtId, LocalDate date, LocalTime endTime, LocalTime startTime, Collection<String> statuses);

    List<ReservationPersistenceEntity> findByCourtId(Long courtId);
}
