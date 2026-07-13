package com.partidoya.platform.iam.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public record ActivateUserCommand(UserId adminId, UserId targetUserId, String reason) {
}
