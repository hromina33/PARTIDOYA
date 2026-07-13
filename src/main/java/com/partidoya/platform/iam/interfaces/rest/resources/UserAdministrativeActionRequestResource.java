package com.partidoya.platform.iam.interfaces.rest.resources;

import java.time.LocalDate;

public record UserAdministrativeActionRequestResource(
        Long adminId,
        String reason,
        LocalDate suspendedUntil
) {
}
