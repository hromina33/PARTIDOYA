package com.partidoya.platform.iam.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.FullName;
import com.partidoya.platform.iam.domain.model.valueobjects.RawPassword;
import com.partidoya.platform.iam.domain.model.valueobjects.Role;

import java.util.Objects;

public record RegisterUserCommand(
        EmailAddress email,
        FullName fullName,
        RawPassword password,
        Role role) {
    public RegisterUserCommand {
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(fullName, "fullName cannot be null");
        Objects.requireNonNull(password, "password cannot be null");
        Objects.requireNonNull(role, "role cannot be null");
    }
}
