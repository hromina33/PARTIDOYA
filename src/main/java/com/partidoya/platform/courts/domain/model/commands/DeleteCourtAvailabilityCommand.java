package com.partidoya.platform.courts.domain.model.commands;

import com.partidoya.platform.courts.domain.model.valueobjects.CourtAvailabilityId;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public record DeleteCourtAvailabilityCommand(UserId requesterId, CourtAvailabilityId availabilityId) {
}
