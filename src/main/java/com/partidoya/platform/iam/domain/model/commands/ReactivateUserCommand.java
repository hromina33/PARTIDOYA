package com.partidoya.platform.iam.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public record ReactivateUserCommand(UserId adminId, UserId targetUserId, String reason) {
}
