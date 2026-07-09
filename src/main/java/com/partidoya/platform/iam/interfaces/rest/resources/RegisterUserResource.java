package com.partidoya.platform.iam.interfaces.rest.resources;

public record RegisterUserResource(String email, String fullName, String password, String role) {
}
