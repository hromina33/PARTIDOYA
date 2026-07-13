package com.partidoya.platform.iam.interfaces.rest.transform;

import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.interfaces.rest.resources.UserResource;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
                user.getId().value(),
                user.getEmail().value(),
                user.getFullName().value(),
                user.isEmailVerified(),
                user.getRole().name(),
                user.getPlan().name(),
                user.getStatus().name(),
                user.getSuspensionReason(),
                user.getSuspendedUntil(),
                user.getLastAdministrativeActionAt());
    }
}
