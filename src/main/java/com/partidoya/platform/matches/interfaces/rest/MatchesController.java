package com.partidoya.platform.matches.interfaces.rest;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.application.commandservices.MatchCommandService;
import com.partidoya.platform.matches.application.queryservices.MatchQueryService;
import com.partidoya.platform.matches.domain.model.commands.CancelMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.JoinMatchCommand;
import com.partidoya.platform.matches.domain.model.commands.LeaveMatchCommand;
import com.partidoya.platform.matches.domain.model.queries.GetAllOpenMatchesQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchByIdQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchesByOrganizerQuery;
import com.partidoya.platform.matches.domain.model.queries.GetMatchesByParticipantQuery;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.interfaces.rest.resources.CancelMatchResource;
import com.partidoya.platform.matches.interfaces.rest.resources.CreateMatchResource;
import com.partidoya.platform.matches.interfaces.rest.resources.JoinMatchResource;
import com.partidoya.platform.matches.interfaces.rest.resources.LeaveMatchResource;
import com.partidoya.platform.matches.interfaces.rest.resources.MatchResource;
import com.partidoya.platform.matches.interfaces.rest.transform.CreateMatchCommandFromResourceAssembler;
import com.partidoya.platform.matches.interfaces.rest.transform.MatchResourceFromEntityAssembler;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/matches", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Matches", description = "Match management endpoints")
public class MatchesController {
    private final MatchCommandService matchCommandService;
    private final MatchQueryService matchQueryService;

    public MatchesController(MatchCommandService matchCommandService, MatchQueryService matchQueryService) {
        this.matchCommandService = matchCommandService;
        this.matchQueryService = matchQueryService;
    }

    @PostMapping
    public ResponseEntity<MatchResource> createMatch(@RequestBody CreateMatchResource resource) {
        var command = CreateMatchCommandFromResourceAssembler.toCommandFromResource(resource);
        var match = matchCommandService.handle(command);
        var body = MatchResourceFromEntityAssembler.toResourceFromEntity(match);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    @GetMapping
    public ResponseEntity<List<MatchResource>> getOpenMatches(
            @RequestParam(required = false) String sport) {
        var query = new GetAllOpenMatchesQuery(sport);
        var matches = matchQueryService.handle(query);
        var body = matches.stream()
                .map(MatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<MatchResource> getMatchById(@PathVariable Long matchId) {
        var query = new GetMatchByIdQuery(new MatchId(matchId));
        var match = matchQueryService.handle(query)
                .orElseThrow(() -> new ResourceNotFoundException("Match", matchId.toString()));
        var body = MatchResourceFromEntityAssembler.toResourceFromEntity(match);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{matchId}/join")
    public ResponseEntity<MatchResource> joinMatch(
            @PathVariable Long matchId,
            @RequestBody JoinMatchResource resource) {
        var command = new JoinMatchCommand(new MatchId(matchId), new UserId(resource.userId()));
        var match = matchCommandService.handle(command);
        var body = MatchResourceFromEntityAssembler.toResourceFromEntity(match);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/organized/{userId}")
    public ResponseEntity<List<MatchResource>> getMatchesByOrganizer(@PathVariable Long userId) {
        var query = new GetMatchesByOrganizerQuery(new UserId(userId));
        var matches = matchQueryService.handle(query);
        var body = matches.stream()
                .map(MatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/joined/{userId}")
    public ResponseEntity<List<MatchResource>> getMatchesByParticipant(@PathVariable Long userId) {
        var query = new GetMatchesByParticipantQuery(new UserId(userId));
        var matches = matchQueryService.handle(query);
        var body = matches.stream()
                .map(MatchResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{matchId}/leave")
    public ResponseEntity<MatchResource> leaveMatch(
            @PathVariable Long matchId,
            @RequestBody LeaveMatchResource resource) {
        var command = new LeaveMatchCommand(new MatchId(matchId), new UserId(resource.userId()));
        var match = matchCommandService.handle(command);
        var body = MatchResourceFromEntityAssembler.toResourceFromEntity(match);
        return ResponseEntity.ok(body);
    }

    @PostMapping("/{matchId}/cancel")
    public ResponseEntity<MatchResource> cancelMatch(
            @PathVariable Long matchId,
            @RequestBody CancelMatchResource resource) {
        var command = new CancelMatchCommand(new MatchId(matchId), new UserId(resource.requesterId()));
        var match = matchCommandService.handle(command);
        var body = MatchResourceFromEntityAssembler.toResourceFromEntity(match);
        return ResponseEntity.ok(body);
    }
}
