package com.partidoya.platform.iam.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.RawPassword;

import java.util.Objects;

public record AuthenticateUserCommand(
        EmailAddress email,
        RawPassword password) {
    public AuthenticateUserCommand {
        Objects.requireNonNull(email, "email cannot be null");
        Objects.requireNonNull(password, "password cannot be null");
    }
}
