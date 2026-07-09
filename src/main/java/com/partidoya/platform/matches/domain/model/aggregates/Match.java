package com.partidoya.platform.matches.domain.model.aggregates;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.commands.CreateMatchCommand;
import com.partidoya.platform.matches.domain.model.valueobjects.Location;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDate;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDescription;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchId;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchPrice;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchStatus;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchTitle;
import com.partidoya.platform.matches.domain.model.valueobjects.TotalSlots;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
public class Match {
    private MatchId id;
    private UserId organizerId;
    private String sport;
    private MatchTitle title;
    private MatchDescription description;
    private Location address;
    private MatchDate matchDate;
    private TotalSlots totalSlots;
    private MatchPrice price;
    private MatchStatus status;
    private List<UserId> participants;

    protected Match() {
    }

    public Match(UserId organizerId, String sport, MatchTitle title, MatchDescription description,
                 Location address, MatchDate matchDate, TotalSlots totalSlots, MatchPrice price) {
        this.organizerId = Objects.requireNonNull(organizerId, "organizerId must not be null");
        this.sport = Objects.requireNonNull(sport, "sport must not be null");
        this.title = Objects.requireNonNull(title, "title must not be null");
        this.description = description;
        this.address = Objects.requireNonNull(address, "address must not be null");
        this.matchDate = Objects.requireNonNull(matchDate, "matchDate must not be null");
        this.totalSlots = Objects.requireNonNull(totalSlots, "totalSlots must not be null");
        this.price = price;
        this.status = MatchStatus.OPEN;
        this.participants = new ArrayList<>();
    }

    public Match(CreateMatchCommand command) {
        this(command.organizerId(), command.sport(), command.title(), command.description(),
                command.address(), command.matchDate(), command.totalSlots(), command.price());
    }

    public Match(MatchId id, UserId organizerId, String sport, MatchTitle title, MatchDescription description,
                 Location address, MatchDate matchDate, TotalSlots totalSlots, MatchPrice price, MatchStatus status,
                 List<UserId> participants) {
        this(organizerId, sport, title, description, address, matchDate, totalSlots, price);
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.status = status;
        this.participants = participants != null ? new ArrayList<>(participants) : new ArrayList<>();
    }

    public void join(UserId userId) {
        if (status == MatchStatus.CANCELLED) {
            throw new IllegalStateException("Cannot join a cancelled match");
        }
        if (status == MatchStatus.FULL) {
            throw new IllegalStateException("No available slots");
        }
        if (participants.stream().anyMatch(p -> p.value().equals(userId.value()))) {
            throw new IllegalStateException("User already joined this match");
        }
        participants.add(userId);
        if (participants.size() >= totalSlots.value()) {
            status = MatchStatus.FULL;
        }
    }

    public void leave(UserId userId) {
        if (status == MatchStatus.CANCELLED) {
            throw new IllegalStateException("Cannot leave a cancelled match");
        }
        if (organizerId.value().equals(userId.value())) {
            throw new IllegalStateException("The organizer cannot leave the match");
        }
        boolean removed = participants.removeIf(p -> p.value().equals(userId.value()));
        if (!removed) {
            throw new IllegalStateException("User is not a participant of this match");
        }
        if (status == MatchStatus.FULL) {
            status = MatchStatus.OPEN;
        }
    }

    public void cancel(UserId requesterId) {
        if (!organizerId.value().equals(requesterId.value())) {
            throw new IllegalStateException("Only the organizer can cancel the match");
        }
        if (status == MatchStatus.CANCELLED) {
            throw new IllegalStateException("Match is already cancelled");
        }
        if (matchDate.value().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Cannot cancel a match that has already occurred");
        }
        status = MatchStatus.CANCELLED;
    }

    public List<UserId> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    public int getAvailableSlots() {
        return totalSlots.value() - participants.size();
    }
}
