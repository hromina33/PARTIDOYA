package com.partidoya.platform.iam.interfaces.rest.transform;

import com.partidoya.platform.iam.domain.model.commands.AuthenticateUserCommand;
import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.RawPassword;
import com.partidoya.platform.iam.interfaces.rest.resources.LoginUserResource;

public class AuthenticateUserCommandFromResourceAssembler {
    public static AuthenticateUserCommand toCommandFromResource(LoginUserResource resource) {
        return new AuthenticateUserCommand(
                new EmailAddress(resource.email()),
                new RawPassword(resource.password()));
    }
}
