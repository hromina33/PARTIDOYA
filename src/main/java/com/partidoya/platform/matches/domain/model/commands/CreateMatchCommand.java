package com.partidoya.platform.matches.domain.model.commands;

import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import com.partidoya.platform.matches.domain.model.valueobjects.Location;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDate;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchDescription;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchPrice;
import com.partidoya.platform.matches.domain.model.valueobjects.MatchTitle;
import com.partidoya.platform.matches.domain.model.valueobjects.TotalSlots;

import java.time.LocalDateTime;
import java.util.Objects;

public record CreateMatchCommand(
        UserId organizerId,
        Long courtReservationId,
        String sport,
        MatchTitle title,
        MatchDescription description,
        Location address,
        MatchDate matchDate,
        TotalSlots totalSlots,
        MatchPrice price,
        Double latitude,
        Double longitude,
        boolean requiresPlayerPayment,
        String yapePhone) {
    public CreateMatchCommand {
        Objects.requireNonNull(organizerId, "organizerId cannot be null");
        Objects.requireNonNull(sport, "sport cannot be null");
        Objects.requireNonNull(title, "title cannot be null");
        Objects.requireNonNull(address, "address cannot be null");
        Objects.requireNonNull(matchDate, "matchDate cannot be null");
        Objects.requireNonNull(totalSlots, "totalSlots cannot be null");
        if (matchDate.value().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("matchDate must be in the future");
        }
    }
}
