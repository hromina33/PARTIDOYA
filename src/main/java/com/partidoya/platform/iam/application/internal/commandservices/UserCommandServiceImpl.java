package com.partidoya.platform.iam.application.internal.commandservices;

import com.partidoya.platform.iam.application.commandservices.UserCommandService;
import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.commands.AuthenticateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.RegisterUserCommand;
import com.partidoya.platform.iam.domain.model.commands.UpdateFullNameCommand;
import com.partidoya.platform.iam.domain.model.commands.UpdatePlanCommand;
import com.partidoya.platform.iam.domain.model.valueobjects.HashedPassword;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.iam.domain.services.PasswordHasher;
import com.partidoya.platform.shared.domain.exceptions.ResourceConflictException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;

    public UserCommandServiceImpl(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
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
}
