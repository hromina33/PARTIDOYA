package com.partidoya.platform.courts.interfaces.rest;

import com.partidoya.platform.courts.application.commandservices.CourtCommandService;
import com.partidoya.platform.courts.application.queryservices.CourtQueryService;
import com.partidoya.platform.courts.domain.model.commands.PublishCourtCommand;
import com.partidoya.platform.courts.domain.model.commands.ReserveCourtCommand;
import com.partidoya.platform.courts.domain.model.queries.GetCourtByIdQuery;
import com.partidoya.platform.courts.domain.model.queries.GetManagedCourtsQuery;
import com.partidoya.platform.courts.domain.model.queries.SearchPublishedCourtsQuery;
import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.courts.interfaces.rest.resources.CourtResource;
import com.partidoya.platform.courts.interfaces.rest.resources.CreateCourtResource;
import com.partidoya.platform.courts.interfaces.rest.resources.PublishCourtResource;
import com.partidoya.platform.courts.interfaces.rest.resources.ReservationResource;
import com.partidoya.platform.courts.interfaces.rest.resources.ReserveCourtResource;
import com.partidoya.platform.courts.interfaces.rest.resources.UpdateCourtResource;
import com.partidoya.platform.courts.interfaces.rest.transform.CourtCommandFromResourceAssembler;
import com.partidoya.platform.courts.interfaces.rest.transform.CourtResourceFromEntityAssembler;
import com.partidoya.platform.courts.interfaces.rest.transform.ReservationResourceFromEntityAssembler;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.shared.domain.exceptions.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/courts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Courts", description = "Court publication and reservation endpoints")
public class CourtsController {
    private final CourtCommandService courtCommandService;
    private final CourtQueryService courtQueryService;

    public CourtsController(CourtCommandService courtCommandService, CourtQueryService courtQueryService) {
        this.courtCommandService = courtCommandService;
        this.courtQueryService = courtQueryService;
    }

    @GetMapping
    public ResponseEntity<List<CourtResource>> searchPublishedCourts(
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) String district) {
        var courts = courtQueryService.handle(new SearchPublishedCourtsQuery(sport, district));
        var body = courts.stream().map(CourtResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(body);
    }

    @GetMapping("/{courtId}")
    public ResponseEntity<CourtResource> getCourtById(@PathVariable Long courtId) {
        var court = courtQueryService.handle(new GetCourtByIdQuery(new CourtId(courtId)))
                .orElseThrow(() -> new ResourceNotFoundException("Court", courtId.toString()));
        return ResponseEntity.ok(CourtResourceFromEntityAssembler.toResourceFromEntity(court));
    }

    @GetMapping("/managed/{ownerId}")
    public ResponseEntity<List<CourtResource>> getManagedCourts(@PathVariable Long ownerId) {
        var courts = courtQueryService.handle(new GetManagedCourtsQuery(new UserId(ownerId)));
        var body = courts.stream().map(CourtResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<CourtResource> createCourt(@RequestBody CreateCourtResource resource) {
        var court = courtCommandService.handle(CourtCommandFromResourceAssembler.toCommandFromResource(resource));
        return ResponseEntity.status(HttpStatus.CREATED).body(CourtResourceFromEntityAssembler.toResourceFromEntity(court));
    }

    @PutMapping("/{courtId}")
    public ResponseEntity<CourtResource> updateCourt(@PathVariable Long courtId, @RequestBody UpdateCourtResource resource) {
        var court = courtCommandService.handle(CourtCommandFromResourceAssembler.toCommandFromResource(courtId, resource));
        return ResponseEntity.ok(CourtResourceFromEntityAssembler.toResourceFromEntity(court));
    }

    @PostMapping("/{courtId}/publication")
    public ResponseEntity<CourtResource> publishCourt(@PathVariable Long courtId, @RequestBody PublishCourtResource resource) {
        var court = courtCommandService.handle(new PublishCourtCommand(new CourtId(courtId),
                new UserId(resource.requesterId()), resource.published()));
        return ResponseEntity.ok(CourtResourceFromEntityAssembler.toResourceFromEntity(court));
    }

    @PostMapping("/{courtId}/reservations")
    public ResponseEntity<ReservationResource> reserveCourt(@PathVariable Long courtId, @RequestBody ReserveCourtResource resource) {
        var reservation = courtCommandService.handle(new ReserveCourtCommand(new CourtId(courtId),
                new UserId(resource.userId()), resource.date(), resource.startTime(), resource.endTime(),
                resource.paymentMethod(), resource.culqiToken(), resource.payerEmail(), resource.idempotencyKey()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservationResourceFromEntityAssembler.toResourceFromEntity(reservation));
    }
}
