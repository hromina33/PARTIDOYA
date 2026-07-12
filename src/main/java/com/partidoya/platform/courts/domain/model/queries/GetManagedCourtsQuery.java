package com.partidoya.platform.courts.domain.model.queries;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public record GetManagedCourtsQuery(UserId ownerId) {
}
