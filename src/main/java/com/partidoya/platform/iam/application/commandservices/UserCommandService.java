package com.partidoya.platform.iam.application.commandservices;

import com.partidoya.platform.iam.domain.model.aggregates.User;
import com.partidoya.platform.iam.domain.model.commands.AuthenticateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.ActivateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.ReactivateUserCommand;
import com.partidoya.platform.iam.domain.model.commands.RegisterUserCommand;
import com.partidoya.platform.iam.domain.model.commands.SuspendUserCommand;
import com.partidoya.platform.iam.domain.model.commands.UpdateFullNameCommand;
import com.partidoya.platform.iam.domain.model.commands.UpdatePlanCommand;

public interface UserCommandService {
    User handle(RegisterUserCommand command);
    User handle(AuthenticateUserCommand command);
    User handle(UpdateFullNameCommand command);
    User handle(UpdatePlanCommand command);
    User handle(ActivateUserCommand command);
    User handle(SuspendUserCommand command);
    User handle(ReactivateUserCommand command);
}
