package com.partidoya.platform.matches.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;

import java.util.Objects;

public record CancelMatchCommand(MatchId matchId, UserId requesterId) {
    public CancelMatchCommand {
        Objects.requireNonNull(matchId, "matchId cannot be null");
        Objects.requireNonNull(requesterId, "requesterId cannot be null");
    }
}
