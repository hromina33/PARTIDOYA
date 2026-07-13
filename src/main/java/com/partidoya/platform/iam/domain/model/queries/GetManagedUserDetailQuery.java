package com.partidoya.platform.iam.domain.model.queries;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public record GetManagedUserDetailQuery(UserId adminId, UserId targetUserId) {
}
