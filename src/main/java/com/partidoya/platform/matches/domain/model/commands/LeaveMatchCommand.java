package com.partidoya.platform.matches.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;

import java.util.Objects;

public record LeaveMatchCommand(MatchId matchId, UserId userId) {
    public LeaveMatchCommand {
        Objects.requireNonNull(matchId, "matchId cannot be null");
        Objects.requireNonNull(userId, "userId cannot be null");
    }
}
