package com.partidoya.platform.matches.application.commandservices;

import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.domain.model.commands.CancelMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.CreateMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.JoinMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.LeaveMatchCommand;

public interface MatchCommandService {
    Match handle(CreateMatchCommand command);
    Match handle(JoinMatchCommand command);
    Match handle(CancelMatchCommand command);
    Match handle(LeaveMatchCommand command);
}
