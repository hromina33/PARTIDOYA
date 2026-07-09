package com.partidoya.platform.iam.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.FullName;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.Objects;

public record UpdateFullNameCommand(UserId userId, FullName fullName) {
    public UpdateFullNameCommand {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(fullName, "fullName cannot be null");
    }
}
