package com.partidoya.platform.profiles.interfaces.rest.transform;

import com.partidoya.platform.profiles.domain.model.aggregates.Profile;
import com.partidoya.platform.profiles.interfaces.rest.resources.ProfileResource;
import com.partidoya.platform.profiles.interfaces.rest.resources.SportPreferenceResource;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResourceFromEntity(Profile profile) {
        var preferences = profile.getSportPreferences().stream()
                .map(sp -> new SportPreferenceResource(
                        sp.sportName().value(),
                        sp.skillLevel().name()))
                .toList();
        var availability = profile.getAvailability().stream()
                .map(Enum::name)
                .toList();
        return new ProfileResource(
                profile.getId().value(),
                profile.getUserId().value(),
                profile.getPhoneNumber() != null ? profile.getPhoneNumber().value() : null,
                profile.getAvatarUrl() != null ? profile.getAvatarUrl().value() : null,
                profile.getPrimarySport() != null ? profile.getPrimarySport().value() : null,
                profile.getSkillLevel() != null ? profile.getSkillLevel().name() : null,
                availability,
                preferences);
    }
}
