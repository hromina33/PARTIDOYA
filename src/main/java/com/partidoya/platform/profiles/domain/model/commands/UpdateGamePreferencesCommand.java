package com.partidoya.platform.profiles.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.profiles.domain.model.valueobjects.SkillLevel;
import com.partidoya.platform.profiles.domain.model.valueobjects.TimeSlot;

import java.util.Objects;
import java.util.Set;

public record UpdateGamePreferencesCommand(UserId userId, SkillLevel skillLevel, Set<TimeSlot> availability) {
    public UpdateGamePreferencesCommand {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(skillLevel, "skillLevel cannot be null");
        if (availability == null) {
            availability = Set.of();
        }
    }
}
