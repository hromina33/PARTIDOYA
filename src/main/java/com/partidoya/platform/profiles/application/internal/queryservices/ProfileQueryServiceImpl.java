package com.partidoya.platform.profiles.application.internal.queryservices;

import com.partidoya.platform.profiles.application.queryservices.ProfileQueryService;
import com.partidoya.platform.profiles.domain.model.aggregates.Profile;
import com.partidoya.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.partidoya.platform.profiles.domain.model.queries.GetProfileByUserIdQuery;
import com.partidoya.platform.profiles.domain.repositories.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {
    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Optional<Profile> handle(GetProfileByIdQuery query) {
        return profileRepository.findById(query.profileId());
    }

    @Override
    public Optional<Profile> handle(GetProfileByUserIdQuery query) {
        return profileRepository.findByUserId(query.userId());
    }
}
