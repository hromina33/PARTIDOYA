package com.partidoya.platform.iam.domain.model.queries;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

public record SearchManagedUsersQuery(
        UserId adminId,
        String search,
        String roleFilter,
        int page,
        int size
) {
}
