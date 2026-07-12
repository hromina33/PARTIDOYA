package com.partidoya.platform.matches.interfaces.rest.resources;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PlayerJoinRequestResource(
        Long id,
        Long matchId,
        Long playerId,
        String proofUrl,
        String originalFileName,
        BigDecimal amount,
        String status,
        LocalDateTime submittedAt,
        LocalDateTime reviewedAt,
        Long reviewedBy,
        String rejectionReason
) {
}
