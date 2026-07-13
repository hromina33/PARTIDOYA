package com.partidoya.platform.iam.interfaces.rest.resources;

import java.time.LocalDateTime;

public record AdminUserActionResource(
        Long id,
        Long adminId,
        String action,
        String reason,
        LocalDateTime createdAt
) {
}
