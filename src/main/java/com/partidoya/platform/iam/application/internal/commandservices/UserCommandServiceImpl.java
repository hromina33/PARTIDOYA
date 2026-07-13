package com.partidoya.platform.iam.application.internal.commandservices;

import com.partidoya.platform.iam.application.commandservices.UserCommandService;
import com.partidoya.platform.iam.domain.model.aggregates.UserAdministrativeAction;
import com.partidoya.platform.iam.domain.model.commands.ActivateUserCommand;
import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.commands.AuthenticateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.ReactivateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.RegisterUserCommand;
import com.partidoya.platform.iam.domain.model.commands.SuspendUserCommand;
import com.partidoya.platform.iam.domain.model.commands.UpdateFullNameCommand;
import com.partidoya.platform.iam.domain.model.commands.UpdatePlanCommand;
import com.partidoya.platform.iam.domain.model.valueobjects.HashedPassword;
import com.partidoya.platform.iam.domain.repositories.UserAdministrativeActionRepository;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.iam.domain.services.PasswordHasher;
import com.partidoya.platform.iam.domain.services.PlanPolicy;
import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceConflictException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final UserAdministrativeActionRepository actionRepository;
    private final PasswordHasher passwordHasher;

    public UserCommandServiceImpl(UserRepository userRepository, UserAdministrativeActionRepository actionRepository,
                                  PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.actionRepository = actionRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public User handle(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ResourceConflictException("User", "Email '%s' is already registered".formatted(command.email().value()));
        }
        var hashedPassword = new HashedPassword(passwordHasher.hash(command.password().value()));
        var user = new User(command.email(), command.fullName(), hashedPassword, command.role());
        return userRepository.save(user);
    }

    @Override
    public User handle(AuthenticateUserCommand command) {
        var user = userRepository.findByEmail(command.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", command.email().value()));
        if (!passwordHasher.matches(command.password().value(), user.getPassword().value())) {
            throw new IllegalArgumentException("Invalid credentials");
        }
        if (!user.isActive()) {
            throw new ForbiddenActionException("Account is suspended");
        }
        return user;
    }

    @Override
    public User handle(UpdateFullNameCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", command.userId().value().toString()));
        user.updateFullName(command.fullName());
        return userRepository.save(user);
    }

    @Override
    public User handle(UpdatePlanCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", command.userId().value().toString()));
        user.updatePlan(command.plan());
        return userRepository.save(user);
    }

    @Override
    public User handle(ActivateUserCommand command) {
        ensurePlatformAdmin(command.adminId());
        var user = findTarget(command.targetUserId());
        user.activate(command.adminId());
        actionRepository.save(new UserAdministrativeAction(command.adminId(), command.targetUserId(), "ACTIVATE", command.reason()));
        return userRepository.save(user);
    }

    @Override
    public User handle(SuspendUserCommand command) {
        ensurePlatformAdmin(command.adminId());
        if (command.adminId().value().equals(command.targetUserId().value())) {
            throw new IllegalArgumentException("Admin cannot suspend own account");
        }
        var user = findTarget(command.targetUserId());
        user.suspend(command.adminId(), command.reason(), command.suspendedUntil());
        actionRepository.save(new UserAdministrativeAction(command.adminId(), command.targetUserId(), "SUSPEND", command.reason()));
        return userRepository.save(user);
    }

    @Override
    public User handle(ReactivateUserCommand command) {
        ensurePlatformAdmin(command.adminId());
        var user = findTarget(command.targetUserId());
        user.reactivate(command.adminId(), command.reason());
        actionRepository.save(new UserAdministrativeAction(command.adminId(), command.targetUserId(), "REACTIVATE", command.reason()));
        return userRepository.save(user);
    }

    private void ensurePlatformAdmin(com.partidoya.platform.iam.domain.model.valueobjects.UserId adminId) {
        var admin = userRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("User", adminId.value().toString()));
        if (!PlanPolicy.canManagePlatform(admin)) {
            throw new ForbiddenActionException("Only platform administrators can manage users");
        }
    }

    private User findTarget(com.partidoya.platform.iam.domain.model.valueobjects.UserId userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId.value().toString()));
    }
}
