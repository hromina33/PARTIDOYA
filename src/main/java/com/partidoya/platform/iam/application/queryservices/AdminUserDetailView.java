package com.partidoya.platform.iam.application.queryservices;

import java.util.List;

public record AdminUserDetailView(
        AdminUserView user,
        List<AdminUserActionView> actions
) {
}
