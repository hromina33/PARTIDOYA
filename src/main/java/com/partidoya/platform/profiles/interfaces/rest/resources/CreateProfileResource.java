package com.partidoya.platform.profiles.interfaces.rest.resources;

import java.util.List;

public record CreateProfileResource(
        Long userId,
        String phoneNumber,
        String avatarUrl,
        String primarySport,
        List<SportPreferenceResource> sportPreferences) {
}
