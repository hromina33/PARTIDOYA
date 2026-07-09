package com.partidoya.platform.matches.infrastructure.persistence.jpa.assemblers;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.domain.model.valueobjects.Location;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDate;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDescription;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchPrice;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchStatus;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchTitle;
import com.partidoya.platform.matches.domain.model.valueobjects.TotalSlots;
import com.partidoya.platform.matches.infrastructure.persistence.jpa.entities.MatchPersistenceEntity;

import java.util.ArrayList;

public final class MatchPersistenceAssembler {

    private MatchPersistenceAssembler() {
    }

    public static Match toDomainFromPersistence(MatchPersistenceEntity entity) {
        if (entity == null) return null;
        var participants = entity.getParticipantIds().stream()
                .map(UserId::new)
                .toList();
        return new Match(
                new MatchId(entity.getId()),
                new UserId(entity.getOrganizerId()),
                entity.getSport(),
                new MatchTitle(entity.getTitle()),
                new MatchDescription(entity.getDescription()),
                new Location(entity.getAddress()),
                new MatchDate(entity.getMatchDate()),
                new TotalSlots(entity.getTotalSlots()),
                new MatchPrice(entity.getPrice()),
                MatchStatus.valueOf(entity.getStatus()),
                participants);
    }

    public static MatchPersistenceEntity toPersistenceFromDomain(Match match) {
        if (match == null) return null;
        var entity = new MatchPersistenceEntity();
        if (match.getId() != null) {
            entity.setId(match.getId().value());
        }
        entity.setOrganizerId(match.getOrganizerId().value());
        entity.setSport(match.getSport());
        entity.setTitle(match.getTitle().value());
        entity.setDescription(match.getDescription() != null ? match.getDescription().value() : null);
        entity.setAddress(match.getAddress().value());
        entity.setMatchDate(match.getMatchDate().value());
        entity.setTotalSlots(match.getTotalSlots().value());
        entity.setPrice(match.getPrice() != null ? match.getPrice().value() : null);
        entity.setStatus(match.getStatus().name());
        var participantIds = new ArrayList<>(match.getParticipants().stream()
                .map(UserId::value)
                .toList());
        entity.setParticipantIds(participantIds);
        return entity;
    }
}
