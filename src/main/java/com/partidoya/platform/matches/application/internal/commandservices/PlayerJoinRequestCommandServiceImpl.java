package com.partidoya.platform.matches.application.internal.commandservices;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.iam.domain.services.PlanPolicy;
import com.partidoya.platform.matches.application.commandservices.PlayerJoinRequestCommandService;
import com.partidoya.platform.matches.domain.model.aggregates.PlayerJoinRequest;
import com.partidoya.platform.matches.domain.model.valueobjects.JoinRequestStatus;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.repositories.MatchRepository;
import com.partidoya.platform.matches.domain.repositories.PlayerJoinRequestRepository;
import com.partidoya.platform.matches.domain.services.StoredPaymentProof;
import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceConflictException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerJoinRequestCommandServiceImpl implements PlayerJoinRequestCommandService {
    private final PlayerJoinRequestRepository joinRequestRepository;
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public PlayerJoinRequestCommandServiceImpl(PlayerJoinRequestRepository joinRequestRepository,
                                               MatchRepository matchRepository,
                                               UserRepository userRepository) {
        this.joinRequestRepository = joinRequestRepository;
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PlayerJoinRequest submitProof(MatchId matchId, UserId playerId, StoredPaymentProof proof) {
        var user = userRepository.findById(playerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", playerId.value().toString()));
        if (!PlanPolicy.canUsePlayerFeatures(user)) {
            throw new ForbiddenActionException("Only player accounts can submit payment proofs");
        }
        var match = matchRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId.value().toString()));
        if (!match.acceptsPaymentProofs()) {
            throw new ForbiddenActionException("This match does not accept payment proofs");
        }
        if (match.getOrganizerId().value().equals(playerId.value())) {
            throw new ForbiddenActionException("Organizer cannot submit a proof for their own match");
        }
        if (match.getParticipants().stream().anyMatch(id -> id.value().equals(playerId.value()))) {
            throw new ResourceConflictException("PlayerJoinRequest", "player already joined");
        }
        if (joinRequestRepository.existsByMatchIdAndPlayerIdAndStatusIn(matchId, playerId,
                List.of(JoinRequestStatus.PENDING_PAYMENT_VERIFICATION, JoinRequestStatus.CONFIRMED))) {
            throw new ResourceConflictException("PlayerJoinRequest", "player already has an active proof");
        }
        var latest = joinRequestRepository.findLatestByMatchIdAndPlayerId(matchId, playerId);
        if (latest.isPresent() && latest.get().getStatus() == JoinRequestStatus.REJECTED) {
            var request = latest.get();
            request.replaceProof(proof.storageKey(), proof.originalFileName(), proof.contentType(),
                    match.getPlayerPaymentAmount().value());
            return joinRequestRepository.save(request);
        }
        return joinRequestRepository.save(new PlayerJoinRequest(matchId, playerId, proof.storageKey(),
                proof.originalFileName(), proof.contentType(), match.getPlayerPaymentAmount().value()));
    }

    @Override
    public PlayerJoinRequest approve(Long requestId, UserId reviewerId) {
        var request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerJoinRequest", requestId.toString()));
        var match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", request.getMatchId().value().toString()));
        ensureOrganizer(match.getOrganizerId(), reviewerId);
        match.confirmParticipant(request.getPlayerId());
        request.approve(reviewerId);
        matchRepository.save(match);
        return joinRequestRepository.save(request);
    }

    @Override
    public PlayerJoinRequest reject(Long requestId, UserId reviewerId, String reason) {
        var request = joinRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("PlayerJoinRequest", requestId.toString()));
        var match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", request.getMatchId().value().toString()));
        ensureOrganizer(match.getOrganizerId(), reviewerId);
        request.reject(reviewerId, reason);
        return joinRequestRepository.save(request);
    }

    private static void ensureOrganizer(UserId organizerId, UserId reviewerId) {
        if (!organizerId.value().equals(reviewerId.value())) {
            throw new ForbiddenActionException("Only the organizer can review payment proofs");
        }
    }
}
