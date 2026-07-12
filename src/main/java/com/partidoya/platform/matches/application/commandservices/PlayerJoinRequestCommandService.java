package com.partidoya.platform.matches.application.commandservices;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.PlayerJoinRequest;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.services.StoredPaymentProof;

public interface PlayerJoinRequestCommandService {
    PlayerJoinRequest submitProof(MatchId matchId, UserId playerId, StoredPaymentProof proof);
    PlayerJoinRequest approve(Long requestId, UserId reviewerId);
    PlayerJoinRequest reject(Long requestId, UserId reviewerId, String reason);
}
