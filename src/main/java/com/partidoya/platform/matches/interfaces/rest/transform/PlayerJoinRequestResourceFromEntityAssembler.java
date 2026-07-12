package com.partidoya.platform.matches.interfaces.rest.transform;

import com.partidoya.platform.matches.domain.model.aggregates.PlayerJoinRequest;
import com.partidoya.platform.matches.interfaces.rest.resources.PlayerJoinRequestResource;

public final class PlayerJoinRequestResourceFromEntityAssembler {
    private PlayerJoinRequestResourceFromEntityAssembler() {
    }

    public static PlayerJoinRequestResource toResourceFromEntity(PlayerJoinRequest request, Long requesterId) {
        return new PlayerJoinRequestResource(
                request.getId(),
                request.getMatchId().value(),
                request.getPlayerId().value(),
                "/api/v1/matches/%d/join-requests/%d/proof?requesterId=%d"
                        .formatted(request.getMatchId().value(), request.getId(), requesterId),
                request.getOriginalFileName(),
                request.getAmount(),
                request.getStatus().name(),
                request.getSubmittedAt(),
                request.getReviewedAt(),
                request.getReviewedBy() != null ? request.getReviewedBy().value() : null,
                request.getRejectionReason());
    }
}
