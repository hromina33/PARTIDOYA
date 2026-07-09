package com.partidoya.platform.profiles.interfaces.rest;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.profiles.application.commandservices.ProfileCommandService;
import com.partidoya.platform.profiles.application.queryservices.ProfileQueryService;
import com.partidoya.platform.profiles.domain.model.commands.UpdateGamePreferencesCommand;
import com.partidoya.platform.profiles.domain.model.commands.UpdatePrimarySportCommand;
import com.partidoya.platform.profiles.domain.model.queries.GetProfileByIdQuery;
import com.partidoya.platform.profiles.domain.model.queries.GetProfileByUserIdQuery;
import com.partidoya.platform.profiles.domain.model.valueobjects.ProfileId;
import com.partidoya.platform.profiles.domain.model.valueobjects.SkillLevel;
import com.partidoya.platform.profiles.domain.model.valueobjects.SportName;
import com.partidoya.platform.profiles.domain.model.valueobjects.TimeSlot;
import com.partidoya.platform.profiles.interfaces.rest.resources.CreateProfileResource;
import com.partidoya.platform.profiles.interfaces.rest.resources.ProfileResource;
import com.partidoya.platform.profiles.interfaces.rest.resources.UpdateGamePreferencesResource;
import com.partidoya.platform.profiles.interfaces.rest.resources.UpdatePrimarySportResource;
import com.partidoya.platform.profiles.interfaces.rest.transform.CreateProfileCommandFromResourceAssembler;
import com.partidoya.platform.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/profiles", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Profiles", description = "User profile and sport preferences management")
public class ProfilesController {
    private final ProfileCommandService profileCommandService;
    private final ProfileQueryService profileQueryService;

    public ProfilesController(ProfileCommandService profileCommandService, ProfileQueryService profileQueryService) {
        this.profileCommandService = profileCommandService;
        this.profileQueryService = profileQueryService;
    }

    @PostMapping
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateProfileResource resource) {
        var command = CreateProfileCommandFromResourceAssembler.toCommandFromResource(resource);
        var profile = profileCommandService.handle(command);
        var body = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResource> getProfileById(@PathVariable Long profileId) {
        var profile = profileQueryService.handle(new GetProfileByIdQuery(new ProfileId(profileId)));
        return profile
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ProfileResource> getProfileByUserId(@PathVariable Long userId) {
        var profile = profileQueryService.handle(new GetProfileByUserIdQuery(new UserId(userId)));
        return profile
                .map(ProfileResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/user/{userId}/primary-sport")
    public ResponseEntity<ProfileResource> updatePrimarySport(@PathVariable Long userId, @RequestBody UpdatePrimarySportResource resource) {
        var command = new UpdatePrimarySportCommand(new UserId(userId), new SportName(resource.primarySport()));
        var profile = profileCommandService.handle(command);
        var body = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile);
        return ResponseEntity.ok(body);
    }

    @PatchMapping("/user/{userId}/game-preferences")
    public ResponseEntity<ProfileResource> updateGamePreferences(@PathVariable Long userId, @RequestBody UpdateGamePreferencesResource resource) {
        Set<TimeSlot> availability = resource.availability() == null
                ? Set.of()
                : resource.availability().stream().map(TimeSlot::valueOf).collect(Collectors.toSet());
        var command = new UpdateGamePreferencesCommand(new UserId(userId), SkillLevel.valueOf(resource.skillLevel()), availability);
        var profile = profileCommandService.handle(command);
        var body = ProfileResourceFromEntityAssembler.toResourceFromEntity(profile);
        return ResponseEntity.ok(body);
    }
}
