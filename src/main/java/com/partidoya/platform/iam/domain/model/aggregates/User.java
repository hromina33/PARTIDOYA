package com.partidoya.platform.iam.domain.model.aggregates;

import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.FullName;
import com.partidoya.platform.iam.domain.model.valueobjects.HashedPassword;
import com.partidoya.platform.iam.domain.model.valueobjects.Plan;
import com.partidoya.platform.iam.domain.model.valueobjects.Role;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class User {
    private UserId id;
    private EmailAddress email;
    private FullName fullName;
    private HashedPassword password;
    private boolean emailVerified;
    private Role role;
    private Plan plan;

    protected User() {
    }

    public User(EmailAddress email, FullName fullName, HashedPassword password, Role role) {
        this.email = Objects.requireNonNull(email, "email must not be null");
        this.fullName = Objects.requireNonNull(fullName, "fullName must not be null");
        this.password = Objects.requireNonNull(password, "password must not be null");
        this.emailVerified = false;
        this.role = Objects.requireNonNull(role, "role must not be null");
        this.plan = Plan.defaultForRole(this.role);
    }

    public User(UserId id, EmailAddress email, FullName fullName, HashedPassword password, boolean emailVerified,
                Role role, Plan plan) {
        this(email, fullName, password, role);
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.emailVerified = emailVerified;
        this.plan = Objects.requireNonNull(plan, "plan must not be null");
    }

    public void updateFullName(FullName fullName) {
        this.fullName = Objects.requireNonNull(fullName, "fullName must not be null");
    }

    public void updatePlan(Plan plan) {
        Objects.requireNonNull(plan, "plan must not be null");
        if (!plan.isCompatibleWith(this.role)) {
            throw new IllegalArgumentException("Plan '%s' is not available for role '%s'".formatted(plan, this.role));
        }
        this.plan = plan;
    }
}
