package com.partidoya.platform.iam.infrastructure.persistence.jpa.repositories;

import com.partidoya.platform.iam.infrastructure.persistence.jpa.entities.UserAdministrativeActionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAdministrativeActionPersistenceRepository extends JpaRepository<UserAdministrativeActionPersistenceEntity, Long> {
    List<UserAdministrativeActionPersistenceEntity> findByTargetUserIdOrderByActionCreatedAtDesc(Long targetUserId);
}
