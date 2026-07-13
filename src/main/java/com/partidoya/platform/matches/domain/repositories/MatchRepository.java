package com.partidoya.platform.matches.domain.repositories;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchStatus;

import java.util.List;
import java.util.Optional;

public interface MatchRepository {
    Optional<Match> findById(MatchId id);
    Match save(Match match);
    List<Match> findAllByStatus(MatchStatus status);
    List<Match> findAllByStatusAndSport(MatchStatus status, String sport);
    List<Match> findAllByOrganizerId(UserId organizerId);
    List<Match> findAllByParticipantId(UserId participantId);
    long countRelatedToUser(UserId userId);
}
