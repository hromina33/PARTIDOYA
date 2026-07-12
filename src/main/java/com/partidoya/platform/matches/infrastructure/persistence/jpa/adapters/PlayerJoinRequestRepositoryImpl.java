package com.partidoya.platform.matches.infrastructure.persistence.jpa.adapters;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.aggregates.PlayerJoinRequest;
import com.partidoya.platform.matches.domain.model.valueobjects.JoinRequestStatus;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.repositories.PlayerJoinRequestRepository;
import com.partidoya.platform.matches.infrastructure.persistence.jpa.assemblers.PlayerJoinRequestPersistenceAssembler;
import com.partidoya.platform.matches.infrastructure.persistence.jpa.repositories.PlayerJoinRequestPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlayerJoinRequestRepositoryImpl implements PlayerJoinRequestRepository {
    private final PlayerJoinRequestPersistenceRepository repository;

    public PlayerJoinRequestRepositoryImpl(PlayerJoinRequestPersistenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PlayerJoinRequest> findById(Long id) {
        return repository.findById(id).map(PlayerJoinRequestPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<PlayerJoinRequest> findLatestByMatchIdAndPlayerId(MatchId matchId, UserId playerId) {
        return repository.findFirstByMatchIdAndPlayerIdOrderBySubmittedAtDesc(matchId.value(), playerId.value())
                .map(PlayerJoinRequestPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByMatchIdAndPlayerIdAndStatusIn(MatchId matchId, UserId playerId, List<JoinRequestStatus> statuses) {
        return repository.existsByMatchIdAndPlayerIdAndStatusIn(matchId.value(), playerId.value(),
                statuses.stream().map(Enum::name).toList());
    }

    @Override
    public List<PlayerJoinRequest> findByMatchId(MatchId matchId) {
        return repository.findByMatchIdOrderBySubmittedAtDesc(matchId.value()).stream()
                .map(PlayerJoinRequestPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<PlayerJoinRequest> findByPlayerId(UserId playerId) {
        return repository.findByPlayerIdOrderBySubmittedAtDesc(playerId.value()).stream()
                .map(PlayerJoinRequestPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public PlayerJoinRequest save(PlayerJoinRequest joinRequest) {
        var entity = PlayerJoinRequestPersistenceAssembler.toPersistenceFromDomain(joinRequest);
        return PlayerJoinRequestPersistenceAssembler.toDomainFromPersistence(repository.save(entity));
    }
}
