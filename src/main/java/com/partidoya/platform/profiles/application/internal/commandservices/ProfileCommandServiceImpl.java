package com.partidoya.platform.profiles.application.internal.commandservices;

import com.partidoya.platform.profiles.application.commandservices.ProfileCommandService;
import com.partidoya.platform.profiles.domain.model.aggregates.Profile;
import com.partidoya.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.partidoya.platform.profiles.domain.model.commands.UpdateGamePreferencesCommand;
import com.partidoya.platform.profiles.domain.model.commands.UpdatePrimarySportCommand;
import com.partidoya.platform.profiles.domain.repositories.ProfileRepository;
import com.partidoya.platform.shared.domain.exceptions.ResourceConflictException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileCommandServiceImpl implements ProfileCommandService {
    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public Profile handle(CreateProfileCommand command) {
        if (profileRepository.existsByUserId(command.userId())) {
            throw new ResourceConflictException("Profile", "A profile already exists for this user");
        }
        var profile = new Profile(command);
        return profileRepository.save(profile);
    }

    @Override
    public Profile handle(UpdatePrimarySportCommand command) {
        var profile = profileRepository.findByUserId(command.userId())
                .orElseGet(() -> new Profile(command.userId(), null, null, null, List.of()));
        profile.updatePrimarySport(command.primarySport());
        return profileRepository.save(profile);
    }

    @Override
    public Profile handle(UpdateGamePreferencesCommand command) {
        var profile = profileRepository.findByUserId(command.userId())
                .orElseGet(() -> new Profile(command.userId(), null, null, null, List.of()));
        profile.updateSkillLevel(command.skillLevel());
        profile.updateAvailability(command.availability());
        return profileRepository.save(profile);
    }
}
