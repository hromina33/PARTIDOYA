package com.partidoya.platform.iam.application.queryservices;

import com.partidoya.platform.iam.application.queryservices.AdminUserDetailView;
import com.partidoya.platform.iam.application.queryservices.AdminUserPageView;
import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.partidoya.platform.iam.domain.model.queries.GetManagedUserDetailQuery;
import com.partidoya.platform.iam.domain.model.queries.SearchManagedUsersQuery;

import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery query);
    AdminUserPageView handle(SearchManagedUsersQuery query);
    AdminUserDetailView handle(GetManagedUserDetailQuery query);
}
