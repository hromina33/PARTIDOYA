package com.partidoya.platform.matches.application.internal.queryservices;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.application.queryservices.PlayerJoinRequestQueryService;
import com.partidoya.platform.matches.domain.model.aggregates.PlayerJoinRequest;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.repositories.MatchRepository;
import com.partidoya.platform.matches.domain.repositories.PlayerJoinRequestRepository;
import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerJoinRequestQueryServiceImpl implements PlayerJoinRequestQueryService {
    private final PlayerJoinRequestRepository joinRequestRepository;
    private final MatchRepository matchRepository;

    public PlayerJoinRequestQueryServiceImpl(PlayerJoinRequestRepository joinRequestRepository,
                                             MatchRepository matchRepository) {
        this.joinRequestRepository = joinRequestRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<PlayerJoinRequest> findAuthorized(MatchId matchId, UserId requesterId) {
        var match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId.value().toString()));
        if (match.getOrganizerId().value().equals(requesterId.value())) {
            return joinRequestRepository.findByMatchId(matchId);
        }
        return joinRequestRepository.findByMatchId(matchId).stream()
                .filter(request -> request.getPlayerId().value().equals(requesterId.value()))
                .toList();
    }

    @Override
    public Optional<PlayerJoinRequest> findAuthorizedById(Long requestId, UserId requesterId) {
        var request = joinRequestRepository.findById(requestId);
        if (request.isEmpty()) return Optional.empty();
        var match = matchRepository.findById(request.get().getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", request.get().getMatchId().value().toString()));
        var isOrganizer = match.getOrganizerId().value().equals(requesterId.value());
        var isOwner = request.get().getPlayerId().value().equals(requesterId.value());
        if (!isOrganizer && !isOwner) {
            throw new ForbiddenActionException("You cannot access this payment proof");
        }
        return request;
    }
}
