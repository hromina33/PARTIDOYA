package com.partidoya.platform.matches.application.queryservices;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.PlayerJoinRequest;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;

import java.util.List;
import java.util.Optional;

public interface PlayerJoinRequestQueryService {
    List<PlayerJoinRequest> findAuthorized(MatchId matchId, UserId requesterId);
    Optional<PlayerJoinRequest> findAuthorizedById(Long requestId, UserId requesterId);
}
