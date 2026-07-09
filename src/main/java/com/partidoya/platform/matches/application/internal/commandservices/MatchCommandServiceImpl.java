package com.partidoya.platform.matches.application.internal.commandservices;

import com.partidoya.platform.iam.domain.model.valueobjects.Role;
import com.partidoya.platform.iam.domain.repositories.UserRepository;
import com.partidoya.platform.matches.application.commandservices.MatchCommandService;
import com.partidoya.platform.matches.domain.model.aggregates.Match;
import com.partidoya.platform.matches.domain.model.commands.CancelMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.CreateMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.JoinMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.LeaveMatchCommand;
import com.partidoya.platform.matches.domain.repositories.MatchRepository;
import com.partidoya.platform.shared.domain.exceptions.ForbiddenActionException;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MatchCommandServiceImpl implements MatchCommandService {
    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public MatchCommandServiceImpl(MatchRepository matchRepository, UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Match handle(CreateMatchCommand command) {
        var organizer = userRepository.findById(command.organizerId())
                .orElseThrow(() -> new ResourceNotFoundException("User", command.organizerId().value().toString()));
        if (organizer.getRole() != Role.ADMIN_CANCHA) {
            throw new ForbiddenActionException("Only ADMIN_CANCHA accounts can create matches");
        }
        var match = new Match(command);
        return matchRepository.save(match);
    }

    @Override
    public Match handle(JoinMatchCommand command) {
        var match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", command.matchId().value().toString()));
        match.join(command.userId());
        return matchRepository.save(match);
    }

    @Override
    public Match handle(CancelMatchCommand command) {
        var match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", command.matchId().value().toString()));
        match.cancel(command.requesterId());
        return matchRepository.save(match);
    }

    @Override
    public Match handle(LeaveMatchCommand command) {
        var match = matchRepository.findById(command.matchId())
                .orElseThrow(() -> new ResourceNotFoundException("Match", command.matchId().value().toString()));
        match.leave(command.userId());
        return matchRepository.save(match);
    }
}
