package com.partidoya.platform.profiles.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.profiles.domain.model.valueobjects.SportName;

import java.util.Objects;

public record UpdatePrimarySportCommand(UserId userId, SportName primarySport) {
    public UpdatePrimarySportCommand {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(primarySport, "primarySport cannot be null");
    }
}
