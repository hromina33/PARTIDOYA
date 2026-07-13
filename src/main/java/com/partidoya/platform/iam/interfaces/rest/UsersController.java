package com.partidoya.platform.iam.interfaces.rest;

import com.partidoya.platform.iam.application.commandservices.UserCommandService;
import com.partidoya.platform.iam.application.queryservices.UserQueryService;
import com.partidoya.platform.iam.domain.model.commands.ActivateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.ReactivateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.SuspendUserCommand;
import com.partidoya.platform.iam.domain.model.queries.GetManagedUserDetailQuery;
import com.partidoya.platform.iam.domain.model.commands.UpdateFullNameCommand;
import com.partidoya.platform.iam.domain.model.commands.UpdatePlanCommand;
import com.partidoya.platform.iam.domain.model.queries.GetUserByIdQuery;
import com.partidoya.platform.iam.domain.model.queries.SearchManagedUsersQuery;
import com.partidoya.platform.iam.domain.model.valueobjects.FullName;
import com.partidoya.platform.iam.domain.model.valueobjects.Plan;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.iam.interfaces.rest.resources.AdminUserDetailResource;
import com.partidoya.platform.iam.interfaces.rest.resources.AdminUserPageResource;
import com.partidoya.platform.iam.interfaces.rest.resources.UpdateFullNameResource;
import com.partidoya.platform.iam.interfaces.rest.resources.UpdatePlanResource;
import com.partidoya.platform.iam.interfaces.rest.resources.UserAdministrativeActionRequestResource;
import com.partidoya.platform.iam.interfaces.rest.resources.UserResource;
import com.partidoya.platform.iam.interfaces.rest.transform.AdminUserResourceFromEntityAssembler;
import com.partidoya.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User query and update endpoints")
public class UsersController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var user = userQueryService.handle(new GetUserByIdQuery(new UserId(userId)));
        return user
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/admin")
    public ResponseEntity<AdminUserPageResource> searchUsersForAdmin(
            @RequestParam Long adminId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "ALL") String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var result = userQueryService.handle(new SearchManagedUsersQuery(new UserId(adminId), search, role, page, size));
        return ResponseEntity.ok(AdminUserResourceFromEntityAssembler.toResourceFromEntity(result));
    }

    @GetMapping("/admin/{userId}")
    public ResponseEntity<AdminUserDetailResource> getUserDetailForAdmin(
            @PathVariable Long userId,
            @RequestParam Long adminId) {
        var detail = userQueryService.handle(new GetManagedUserDetailQuery(new UserId(adminId), new UserId(userId)));
        return ResponseEntity.ok(AdminUserResourceFromEntityAssembler.toResourceFromEntity(detail));
    }

    @PatchMapping("/admin/{userId}/activate")
    public ResponseEntity<UserResource> activateUser(@PathVariable Long userId,
                                                     @RequestBody UserAdministrativeActionRequestResource resource) {
        var user = userCommandService.handle(new ActivateUserCommand(new UserId(resource.adminId()), new UserId(userId),
                resource.reason()));
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user));
    }

    @PatchMapping("/admin/{userId}/suspend")
    public ResponseEntity<UserResource> suspendUser(@PathVariable Long userId,
                                                    @RequestBody UserAdministrativeActionRequestResource resource) {
        var user = userCommandService.handle(new SuspendUserCommand(new UserId(resource.adminId()), new UserId(userId),
                resource.reason(), resource.suspendedUntil()));
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user));
    }

    @PatchMapping("/admin/{userId}/reactivate")
    public ResponseEntity<UserResource> reactivateUser(@PathVariable Long userId,
                                                       @RequestBody UserAdministrativeActionRequestResource resource) {
        var user = userCommandService.handle(new ReactivateUserCommand(new UserId(resource.adminId()), new UserId(userId),
                resource.reason()));
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResource> updateFullName(@PathVariable Long userId, @RequestBody UpdateFullNameResource resource) {
        var command = new UpdateFullNameCommand(new UserId(userId), new FullName(resource.fullName()));
        var user = userCommandService.handle(command);
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user));
    }

    @PatchMapping("/{userId}/plan")
    public ResponseEntity<UserResource> updatePlan(@PathVariable Long userId, @RequestBody UpdatePlanResource resource) {
        var command = new UpdatePlanCommand(new UserId(userId), Plan.valueOf(resource.plan()));
        var user = userCommandService.handle(command);
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user));
    }
}
