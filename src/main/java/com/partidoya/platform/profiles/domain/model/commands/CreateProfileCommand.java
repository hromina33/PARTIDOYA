package com.partidoya.platform.profiles.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.profiles.domain.model.valueobjects.AvatarUrl;
import com.partidoya.platform.profiles.domain.model.valueobjects.PhoneNumber;
import com.partidoya.platform.profiles.domain.model.valueobjects.SportName;
import com.partidoya.platform.profiles.domain.model.valueobjects.SportPreference;

import java.util.List;
import java.util.Objects;

public record CreateProfileCommand(
        UserId userId,
        PhoneNumber phoneNumber,
        AvatarUrl avatarUrl,
        SportName primarySport,
        List<SportPreference> sportPreferences) {
    public CreateProfileCommand {
        Objects.requireNonNull(userId, "userId cannot be null");
        if (sportPreferences == null) {
            sportPreferences = List.of();
        }
    }
}
