package com.partidoya.platform.iam.domain.repositories;

import com.partidoya.platform.iam.domain.model.aggregates.UserAdministrativeAction;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;

import java.util.List;

public interface UserAdministrativeActionRepository {
    UserAdministrativeAction save(UserAdministrativeAction action);
    List<UserAdministrativeAction> findByTargetUserId(UserId targetUserId);
}
