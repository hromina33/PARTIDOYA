package com.partidoya.platform.profiles.interfaces.rest.transform;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.partidoya.platform.profiles.domain.model.valueobjects.AvatarUrl;
import com.partidoya.platform.profiles.domain.model.valueobjects.PhoneNumber;
import com.partidoya.platform.profiles.domain.model.valueobjects.SkillLevel;
import com.partidoya.platform.profiles.domain.model.valueobjects.SportName;
import com.partidoya.platform.profiles.domain.model.valueobjects.SportPreference;
import com.partidoya.platform.profiles.interfaces.rest.resources.CreateProfileResource;

import java.util.List;

public class CreateProfileCommandFromResourceAssembler {
    public static CreateProfileCommand toCommandFromResource(CreateProfileResource resource) {
        var preferences = resource.sportPreferences() != null
                ? resource.sportPreferences().stream()
                    .map(sp -> new SportPreference(
                            new SportName(sp.sportName()),
                            SkillLevel.valueOf(sp.skillLevel().toUpperCase())))
                    .toList()
                : List.<SportPreference>of();

        return new CreateProfileCommand(
                new UserId(resource.userId()),
                new PhoneNumber(resource.phoneNumber()),
                new AvatarUrl(resource.avatarUrl()),
                resource.primarySport() != null ? new SportName(resource.primarySport()) : null,
                preferences);
    }
}
