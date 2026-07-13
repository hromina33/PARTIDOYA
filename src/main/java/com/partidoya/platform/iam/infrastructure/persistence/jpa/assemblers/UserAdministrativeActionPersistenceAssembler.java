package com.partidoya.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.partidoya.platform.iam.domain.model.aggregates.UserAdministrativeAction;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.iam.infrastructure.persistence.jpa.entities.UserAdministrativeActionPersistenceEntity;

public final class UserAdministrativeActionPersistenceAssembler {
    private UserAdministrativeActionPersistenceAssembler() {
    }

    public static UserAdministrativeAction toDomainFromPersistence(UserAdministrativeActionPersistenceEntity entity) {
        if (entity == null) return null;
        return new UserAdministrativeAction(
                entity.getId(),
                new UserId(entity.getAdminId()),
                new UserId(entity.getTargetUserId()),
                entity.getAction(),
                entity.getReason(),
                entity.getActionCreatedAt());
    }

    public static UserAdministrativeActionPersistenceEntity toPersistenceFromDomain(UserAdministrativeAction action) {
        if (action == null) return null;
        var entity = new UserAdministrativeActionPersistenceEntity();
        if (action.getId() != null) entity.setId(action.getId());
        entity.setAdminId(action.getAdminId().value());
        entity.setTargetUserId(action.getTargetUserId().value());
        entity.setAction(action.getAction());
        entity.setReason(action.getReason());
        entity.setActionCreatedAt(action.getCreatedAt());
        return entity;
    }
}
