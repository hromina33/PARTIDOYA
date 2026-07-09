package com.partidoya.platform.matches.application.queryservices;

import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.domain.model.queries.GetAllOpenMatchesQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchByIdQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchesByOrganizerQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchesByParticipantQuery;

import java.util.List;
import java.util.Optional;

public interface MatchQueryService {
    Optional<Match> handle(GetMatchByIdQuery query);
    List<Match> handle(GetAllOpenMatchesQuery query);
    List<Match> handle(GetMatchesByOrganizerQuery query);
    List<Match> handle(GetMatchesByParticipantQuery query);
}
