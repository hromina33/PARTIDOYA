package com.partidoya.platform.iam.application.queryservices;

import java.time.LocalDateTime;

public record AdminUserActionView(
        Long id,
        Long adminId,
        String action,
        String reason,
        LocalDateTime createdAt
) {
}
