package com.partidoya.platform.courts.application.commandservices;

import com.partidoya.platform.courts.domain.model.aggregates.Court;
import com.partidoya.platform.courts.domain.model.aggregates.Reservation;
import com.partidoya.platform.courts.domain.model.commands.CreateCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.PublishCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.ReserveCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.UpdateCourtCommand;

public interface CourtCommandService {
    Court handle(CreateCourtCommand command);
    Court handle(UpdateCourtCommand command);
    Court handle(PublishCourtCommand command);
    Reservation handle(ReserveCourtCommand command);
}
