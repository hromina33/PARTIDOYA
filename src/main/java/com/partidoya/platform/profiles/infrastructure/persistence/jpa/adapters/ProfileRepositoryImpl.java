package com.partidoya.platform.profiles.infrastructure.persistence.jpa.adapters;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.profiles.domain.model.aggregates.Profile;
import com.partidoya.platform.profiles.domain.model.valueobjects.ProfileId;
import com.partidoya.platform.profiles.domain.repositories.ProfileRepository;
import com.partidoya.platform.profiles.infrastructure.persistence.jpa.assemblers.ProfilePersistenceAssembler;
import com.partidoya.platform.profiles.infrastructure.persistence.jpa.repositories.ProfilePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProfileRepositoryImpl implements ProfileRepository {
    private final ProfilePersistenceRepository profilePersistenceRepository;

    public ProfileRepositoryImpl(ProfilePersistenceRepository profilePersistenceRepository) {
        this.profilePersistenceRepository = profilePersistenceRepository;
    }

    @Override
    public Optional<Profile> findById(ProfileId id) {
        return profilePersistenceRepository.findById(id.value())
                .map(ProfilePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Profile> findByUserId(UserId userId) {
        return profilePersistenceRepository.findByUserId(userId.value())
                .map(ProfilePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Profile save(Profile profile) {
        var entity = ProfilePersistenceAssembler.toPersistenceFromDomain(profile);
        var saved = profilePersistenceRepository.save(entity);
        return ProfilePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsByUserId(UserId userId) {
        return profilePersistenceRepository.existsByUserId(userId.value());
    }
}
