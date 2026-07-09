package com.partidoya.platform.profiles.domain.repositories;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.profiles.domain.model.aggregates.Profile;
import com.partidoya.platform.profiles.domain.model.valueobjects.ProfileId;

import java.util.Optional;

public interface ProfileRepository {
    Optional<Profile> findById(ProfileId id);
    Optional<Profile> findByUserId(UserId userId);
    Profile save(Profile profile);
    boolean existsByUserId(UserId userId);
}
