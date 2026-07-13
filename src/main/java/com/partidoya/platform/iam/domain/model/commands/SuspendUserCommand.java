package com.partidoya.platform.iam.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.time.LocalDate;

public record SuspendUserCommand(UserId adminId, UserId targetUserId, String reason, LocalDate suspendedUntil) {
}
