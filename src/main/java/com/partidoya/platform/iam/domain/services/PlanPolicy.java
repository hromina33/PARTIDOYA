package com.partidoya.platform.iam.domain.services;

import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.valueobjects.Plan;
import com.partidoya.platform.iam.domain.model.valueobjects.Role;

public final class PlanPolicy {
    private PlanPolicy() {
    }

    public static boolean canUsePlayerFeatures(User user) {
        return user != null && user.isActive() && user.getRole() == Role.JUGADOR && isPlayerPlan(user.getPlan());
    }

    public static boolean canManageCourts(User user) {
        return user != null && user.isActive() && user.getRole() == Role.ADMIN_CANCHA && isCourtPlan(user.getPlan());
    }

    public static boolean canManagePlatform(User user) {
        return user != null && user.isActive() && user.getRole() == Role.ADMIN_GENERAL && user.getPlan() == Plan.ADMIN_GENERAL;
    }

    public static boolean isPlayerPlan(Plan plan) {
        return plan == Plan.JUGADOR_BASICO || plan == Plan.JUGADOR_PLUS;
    }

    public static boolean isCourtPlan(Plan plan) {
        return plan == Plan.CANCHA_EMPRENDEDOR || plan == Plan.CANCHA_BUSINESS;
    }
}
