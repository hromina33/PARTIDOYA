package com.partidoya.platform.profiles.interfaces.rest.resources;

import java.util.List;

public record UpdateGamePreferencesResource(String skillLevel, List<String> availability) {
}
