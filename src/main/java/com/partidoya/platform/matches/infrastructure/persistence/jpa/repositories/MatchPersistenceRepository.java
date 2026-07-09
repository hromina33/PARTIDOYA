package com.partidoya.platform.matches.infrastructure.persistence.jpa.repositories;

import com.partidoya.platform.matches.infrastructure.persistence.jpa.entities.MatchPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchPersistenceRepository extends JpaRepository<MatchPersistenceEntity, Long> {
    List<MatchPersistenceEntity> findAllByStatusOrderByMatchDateAsc(String status);
    List<MatchPersistenceEntity> findAllByStatusAndSportIgnoreCaseOrderByMatchDateAsc(String status, String sport);
    List<MatchPersistenceEntity> findAllByOrganizerId(Long organizerId);

    @Query("SELECT m FROM MatchPersistenceEntity m JOIN m.participantIds p WHERE p = :userId")
    List<MatchPersistenceEntity> findAllByParticipantId(@Param("userId") Long userId);
}
