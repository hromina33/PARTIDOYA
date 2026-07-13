package com.partidoya.platform.iam.interfaces.rest.transform;

import com.partidoya.platform.iam.application.queryservices.AdminUserActionView;
import com.partidoya.platform.iam.application.queryservices.AdminUserDetailView;
import com.partidoya.platform.iam.application.queryservices.AdminUserPageView;
import com.partidoya.platform.iam.application.queryservices.AdminUserView;
import com.partidoya.platform.iam.interfaces.rest.resources.AdminUserActionResource;
import com.partidoya.platform.iam.interfaces.rest.resources.AdminUserDetailResource;
import com.partidoya.platform.iam.interfaces.rest.resources.AdminUserPageResource;
import com.partidoya.platform.iam.interfaces.rest.resources.AdminUserResource;

public final class AdminUserResourceFromEntityAssembler {
    private AdminUserResourceFromEntityAssembler() {
    }

    public static AdminUserPageResource toResourceFromEntity(AdminUserPageView page) {
        return new AdminUserPageResource(
                page.content().stream().map(AdminUserResourceFromEntityAssembler::toUser).toList(),
                page.totalElements(),
                page.page(),
                page.size());
    }

    public static AdminUserDetailResource toResourceFromEntity(AdminUserDetailView detail) {
        return new AdminUserDetailResource(
                toUser(detail.user()),
                detail.actions().stream().map(AdminUserResourceFromEntityAssembler::toAction).toList());
    }

    private static AdminUserResource toUser(AdminUserView user) {
        return new AdminUserResource(
                user.id(),
                user.email(),
                user.fullName(),
                user.role(),
                user.plan(),
                user.status(),
                user.suspensionReason(),
                user.suspendedUntil(),
                user.lastAdministrativeActionAt(),
                user.reservationCount(),
                user.matchCount(),
                user.averageRating());
    }

    private static AdminUserActionResource toAction(AdminUserActionView action) {
        return new AdminUserActionResource(action.id(), action.adminId(), action.action(), action.reason(), action.createdAt());
    }
}
