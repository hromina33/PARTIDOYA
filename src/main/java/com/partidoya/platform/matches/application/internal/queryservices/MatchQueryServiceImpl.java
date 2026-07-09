package com.partidoya.platform.matches.application.internal.queryservices;

import com.partidoya.platform.matches.application.queryservices.MatchQueryService;
import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.domain.model.queries.GetAllOpenMatchesQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchByIdQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchesByOrganizerQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchesByParticipantQuery;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchStatus;
import com.partidoya.platform.matches.domain.repositories.MatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MatchQueryServiceImpl implements MatchQueryService {
    private final MatchRepository matchRepository;

    public MatchQueryServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public Optional<Match> handle(GetMatchByIdQuery query) {
        return matchRepository.findById(query.matchId());
    }

    @Override
    public List<Match> handle(GetAllOpenMatchesQuery query) {
        if (query.sportFilter() != null && !query.sportFilter().isBlank()) {
            return matchRepository.findAllByStatusAndSport(MatchStatus.OPEN, query.sportFilter());
        }
        return matchRepository.findAllByStatus(MatchStatus.OPEN);
    }

    @Override
    public List<Match> handle(GetMatchesByOrganizerQuery query) {
        return matchRepository.findAllByOrganizerId(query.organizerId());
    }

    @Override
    public List<Match> handle(GetMatchesByParticipantQuery query) {
        return matchRepository.findAllByParticipantId(query.participantId());
    }
}
