package com.partidoya.platform.iam.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.Plan;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.Objects;

public record UpdatePlanCommand(UserId userId, Plan plan) {
    public UpdatePlanCommand {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(plan, "plan cannot be null");
    }
}
