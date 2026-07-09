package com.partidoya.platform.matches.interfaces.rest.transform;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.commands.CreateMatchCommand;
import com.partidoya.platform.matches.domain.model.valueobjects.Location;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDate;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDescription;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchPrice;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchTitle;
import com.partidoya.platform.matches.domain.model.valueobjects.TotalSlots;
import com.partidoya.platform.matches.interfaces.rest.resources.CreateMatchResource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class CreateMatchCommandFromResourceAssembler {

    private CreateMatchCommandFromResourceAssembler() {
    }

    public static CreateMatchCommand toCommandFromResource(CreateMatchResource resource) {
        return new CreateMatchCommand(
                new UserId(resource.organizerId()),
                resource.sport(),
                new MatchTitle(resource.title()),
                new MatchDescription(resource.description()),
                new Location(resource.address()),
                new MatchDate(LocalDateTime.parse(resource.matchDate())),
                new TotalSlots(resource.totalSlots()),
                new MatchPrice(resource.price() != null ? BigDecimal.valueOf(resource.price()) : null));
    }
}
