package com.partidoya.platform.matches.interfaces.rest.transform;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.interfaces.rest.resources.MatchResource;

public final class MatchResourceFromEntityAssembler {

    private MatchResourceFromEntityAssembler() {
    }

    public static MatchResource toResourceFromEntity(Match match) {
        return new MatchResource(
                match.getId().value(),
                match.getOrganizerId().value(),
                match.getSport(),
                match.getTitle().value(),
                match.getDescription() != null ? match.getDescription().value() : null,
                match.getAddress().value(),
                match.getMatchDate().value().toString(),
                match.getTotalSlots().value(),
                match.getAvailableSlots(),
                match.getPrice() != null && match.getPrice().value() != null
                        ? match.getPrice().value().doubleValue()
                        : null,
                match.getStatus().name(),
                match.getParticipants().stream().map(UserId::value).toList());
    }
}
