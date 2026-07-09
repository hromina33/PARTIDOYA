package com.partidoya.platform.iam.domain.model.valueobjects;

public enum Plan {
    JUGADOR_BASICO(Role.JUGADOR),
    JUGADOR_PLUS(Role.JUGADOR),
    CANCHA_EMPRENDEDOR(Role.ADMIN_CANCHA),
    CANCHA_BUSINESS(Role.ADMIN_CANCHA);

    private final Role role;

    Plan(Role role) {
        this.role = role;
    }

    public boolean isCompatibleWith(Role role) {
        return this.role == role;
    }

    public static Plan defaultForRole(Role role) {
        return role == Role.ADMIN_CANCHA ? CANCHA_EMPRENDEDOR : JUGADOR_BASICO;
    }
}
