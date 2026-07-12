package com.partidoya.platform.matches.domain.repositories;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.PlayerJoinRequest;
import com.partidoya.platform.matches.domain.model.valueobjects.JoinRequestStatus;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;

import java.util.List;
import java.util.Optional;

public interface PlayerJoinRequestRepository {
    Optional<PlayerJoinRequest> findById(Long id);
    Optional<PlayerJoinRequest> findLatestByMatchIdAndPlayerId(MatchId matchId, UserId playerId);
    boolean existsByMatchIdAndPlayerIdAndStatusIn(MatchId matchId, UserId playerId, List<JoinRequestStatus> statuses);
    List<PlayerJoinRequest> findByMatchId(MatchId matchId);
    List<PlayerJoinRequest> findByPlayerId(UserId playerId);
    PlayerJoinRequest save(PlayerJoinRequest joinRequest);
}
