package com.partidoya.platform.profiles.application.commandservices;

import com.partidoya.platform.profiles.domain.model.aggregates.Profile;
import com.partidoya.platform.profiles.domain.model.commands.CreateProfileCommand;
import com.partidoya.platform.profiles.domain.model.commands.UpdateGamePreferencesCommand;
import com.partidoya.platform.profiles.domain.model.commands.UpdatePrimarySportCommand;

public interface ProfileCommandService {
    Profile handle(CreateProfileCommand command);
    Profile handle(UpdatePrimarySportCommand command);
    Profile handle(UpdateGamePreferencesCommand command);
}
