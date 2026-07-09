package com.partidoya.platform.profiles.application.queryservices;

import com.partidoya.platform.profiles.domain.model.aggregates.Profile;
import com.partidoya.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.partidoya.platform.profiles.domain.model.queries.GetProfileByUserIdQuery;

import java.util.Optional;

public interface ProfileQueryService {
    Optional<Profile> handle(GetProfileByIdQuery query);
    Optional<Profile> handle(GetProfileByUserIdQuery query);
}
