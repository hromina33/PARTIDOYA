package com.partidoya.platform.iam.infrastructure.persistence.jpa.adapters;

import com.partidoya.platform.iam.domain.model.aggregates.UserAdministrativeAction;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.iam.domain.repositories.UserAdministrativeActionRepository;
import com.partidoya.platform.iam.infrastructure.persistence.jpa.assemblers.UserAdministrativeActionPersistenceAssembler;
import com.partidoya.platform.iam.infrastructure.persistence.jpa.repositories.UserAdministrativeActionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAdministrativeActionRepositoryImpl implements UserAdministrativeActionRepository {
    private final UserAdministrativeActionPersistenceRepository repository;

    public UserAdministrativeActionRepositoryImpl(UserAdministrativeActionPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserAdministrativeAction save(UserAdministrativeAction action) {
        return UserAdministrativeActionPersistenceAssembler.toDomainFromPersistence(
                repository.save(UserAdministrativeActionPersistenceAssembler.toPersistenceFromDomain(action)));
    }

    @Override
    public List<UserAdministrativeAction> findByTargetUserId(UserId targetUserId) {
        return repository.findByTargetUserIdOrderByActionCreatedAtDesc(targetUserId.value()).stream()
                .map(UserAdministrativeActionPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
