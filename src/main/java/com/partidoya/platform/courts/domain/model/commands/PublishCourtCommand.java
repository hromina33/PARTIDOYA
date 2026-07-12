package com.partidoya.platform.courts.domain.model.commands;

import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public record PublishCourtCommand(CourtId courtId, UserId requesterId, boolean published) {
}
