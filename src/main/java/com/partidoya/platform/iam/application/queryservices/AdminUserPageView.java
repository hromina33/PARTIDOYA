package com.partidoya.platform.iam.application.queryservices;

import java.util.List;

public record AdminUserPageView(
        List<AdminUserView> content,
        long totalElements,
        int page,
        int size
) {
}
