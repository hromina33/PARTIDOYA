package com.partidoya.platform.iam.interfaces.rest.transform;

import com.partidoya.platform.iam.domain.model.commands.RegisterUserCommand;
import com.partidoya.platform.iam.domain.model.valueobjects.EmailAddress;
import com.partidoya.platform.iam.domain.model.valueobjects.FullName;
import com.partidoya.platform.iam.domain.model.valueobjects.RawPassword;
import com.partidoya.platform.iam.domain.model.valueobjects.Role;
import com.partidoya.platform.iam.interfaces.rest.resources.RegisterUserResource;

public class RegisterUserCommandFromResourceAssembler {
    public static RegisterUserCommand toCommandFromResource(RegisterUserResource resource) {
        var role = resource.role() == null || resource.role().isBlank()
                ? Role.JUGADOR
                : Role.valueOf(resource.role());
        return new RegisterUserCommand(
                new EmailAddress(resource.email()),
                new FullName(resource.fullName()),
                new RawPassword(resource.password()),
                role);
    }
}
