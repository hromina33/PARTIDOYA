package com.partidoya.platform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(String token, Long userId, String email, String fullName, String role, String plan) {
}
