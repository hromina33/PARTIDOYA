package com.partidoya.platform.iam.infrastructure.persistence.jpa.assemblers;

import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.FullName;
import com.partidoya.platform.iam.domain.model.valueobjects.HashedPassword;
import com.partidoya.platform.iam.domain.model.valueobjects.Plan;
import com.partidoya.platform.iam.domain.model.valueobjects.Role;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.iam.domain.model.valueobjects.UserStatus;
import com.partidoya.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

public final class UserPersistenceAssembler {

    private UserPersistenceAssembler() {
    }

    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        if (entity == null) return null;
        return new User(
                new UserId(entity.getId()),
                new EmailAddress(entity.getEmail()),
                new FullName(entity.getFullName()),
                new HashedPassword(entity.getPassword()),
                entity.isEmailVerified(),
                Role.valueOf(entity.getRole()),
                Plan.valueOf(entity.getPlan()),
                entity.getStatus() == null ? UserStatus.ACTIVE : UserStatus.valueOf(entity.getStatus()),
                entity.getSuspensionReason(),
                entity.getSuspendedUntil(),
                entity.getLastAdministrativeActionAt(),
                entity.getLastAdministrativeActionBy() == null ? null : new UserId(entity.getLastAdministrativeActionBy()));
    }

    public static UserPersistenceEntity toPersistenceFromDomain(User user) {
        if (user == null) return null;
        var entity = new UserPersistenceEntity();
        if (user.getId() != null) {
            entity.setId(user.getId().value());
        }
        entity.setEmail(user.getEmail().value());
        entity.setFullName(user.getFullName().value());
        entity.setPassword(user.getPassword().value());
        entity.setEmailVerified(user.isEmailVerified());
        entity.setRole(user.getRole().name());
        entity.setPlan(user.getPlan().name());
        entity.setStatus(user.getStatus().name());
        entity.setSuspensionReason(user.getSuspensionReason());
        entity.setSuspendedUntil(user.getSuspendedUntil());
        entity.setLastAdministrativeActionAt(user.getLastAdministrativeActionAt());
        entity.setLastAdministrativeActionBy(user.getLastAdministrativeActionBy() == null ? null : user.getLastAdministrativeActionBy().value());
        return entity;
    }
}
