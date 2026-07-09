package com.partidoya.platform.iam.interfaces.rest;

import com.partidoya.platform.iam.application.commandservices.UserCommandService;
import com.partidoya.platform.iam.infrastructure.tokens.JwtTokenService;
import com.partidoya.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;
import com.partidoya.platform.iam.interfaces.rest.resources.LoginUserResource;
import com.partidoya.platform.iam.interfaces.rest.resources.RegisterUserResource;
import com.partidoya.platform.iam.interfaces.rest.resources.UserResource;
import com.partidoya.platform.iam.interfaces.rest.transform.AuthenticateUserCommandFromResourceAssembler;
import com.partidoya.platform.iam.interfaces.rest.transform.RegisterUserCommandFromResourceAssembler;
import com.partidoya.platform.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "User registration and login endpoints")
public class AuthController {
    private final UserCommandService userCommandService;
    private final JwtTokenService jwtTokenService;

    public AuthController(UserCommandService userCommandService, JwtTokenService jwtTokenService) {
        this.userCommandService = userCommandService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResource> register(@RequestBody RegisterUserResource resource) {
        var command = RegisterUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        var body = UserResourceFromEntityAssembler.toResourceFromEntity(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticatedUserResource> login(@RequestBody LoginUserResource resource) {
        var command = AuthenticateUserCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);
        var token = jwtTokenService.generateToken(user.getId().value(), user.getEmail().value());
        var body = new AuthenticatedUserResource(
                token,
                user.getId().value(),
                user.getEmail().value(),
                user.getFullName().value(),
                user.getRole().name(),
                user.getPlan().name());
        return ResponseEntity.ok(body);
    }
}
