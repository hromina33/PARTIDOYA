package com.partidoya.platform.iam.interfaces.rest.resources;

public record UserResource(Long id, String email, String fullName, boolean emailVerified, String role, String plan) {
}
