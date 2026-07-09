package com.partidoya.platform.matches.infrastructure.persistence.jpa.adapters;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchStatus;
import com.partidoya.platform.matches.domain.repositories.MatchRepository;
import com.partidoya.platform.matches.infrastructure.persistence.jpa.assemblers.MatchPersistenceAssembler;
import com.partidoya.platform.matches.infrastructure.persistence.jpa.repositories.MatchPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MatchRepositoryImpl implements MatchRepository {
    private final MatchPersistenceRepository matchPersistenceRepository;

    public MatchRepositoryImpl(MatchPersistenceRepository matchPersistenceRepository) {
        this.matchPersistenceRepository = matchPersistenceRepository;
    }

    @Override
    public Optional<Match> findById(MatchId id) {
        return matchPersistenceRepository.findById(id.value())
                .map(MatchPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Match save(Match match) {
        var entity = MatchPersistenceAssembler.toPersistenceFromDomain(match);
        var saved = matchPersistenceRepository.save(entity);
        return MatchPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<Match> findAllByStatus(MatchStatus status) {
        return matchPersistenceRepository.findAllByStatusOrderByMatchDateAsc(status.name()).stream()
                .map(MatchPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Match> findAllByStatusAndSport(MatchStatus status, String sport) {
        return matchPersistenceRepository.findAllByStatusAndSportIgnoreCaseOrderByMatchDateAsc(status.name(), sport).stream()
                .map(MatchPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Match> findAllByOrganizerId(UserId organizerId) {
        return matchPersistenceRepository.findAllByOrganizerId(organizerId.value()).stream()
                .map(MatchPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Match> findAllByParticipantId(UserId participantId) {
        return matchPersistenceRepository.findAllByParticipantId(participantId.value()).stream()
                .map(MatchPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }
}
