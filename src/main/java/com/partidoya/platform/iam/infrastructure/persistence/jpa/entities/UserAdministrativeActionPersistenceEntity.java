package com.partidoya.platform.iam.infrastructure.persistence.jpa.entities;

import com.partidoya.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_administrative_actions")
@Getter
@Setter
@NoArgsConstructor
public class UserAdministrativeActionPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "admin_id", nullable = false)
    private Long adminId;

    @Column(name = "target_user_id", nullable = false)
    private Long targetUserId;

    @Column(nullable = false, length = 40)
    private String action;

    @Column(length = 300)
    private String reason;

    @Column(name = "action_created_at", nullable = false)
    private LocalDateTime actionCreatedAt;
}
