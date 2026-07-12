package com.partidoya.platform.courts.domain.model.aggregates;

import com.partidoya.platform.courts.domain.model.valueobjects.CourtId;
import com.partidoya.platform.courts.domain.model.valueobjects.PaymentStatus;
import com.partidoya.platform.courts.domain.model.valueobjects.ReservationId;
import com.partidoya.platform.courts.domain.model.valueobjects.ReservationStatus;
import com.partidoya.platform.iam.domain.model.valueobjects.UserId;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Getter
public class Reservation {
    private ReservationId id;
    private UserId userId;
    private CourtId courtId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal price;
    private ReservationStatus status;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;

    protected Reservation() {
    }

    public Reservation(UserId userId, CourtId courtId, LocalDate date, LocalTime startTime, LocalTime endTime,
                       BigDecimal price) {
        this.userId = Objects.requireNonNull(userId, "userId must not be null");
        this.courtId = Objects.requireNonNull(courtId, "courtId must not be null");
        this.date = Objects.requireNonNull(date, "date must not be null");
        this.startTime = Objects.requireNonNull(startTime, "startTime must not be null");
        this.endTime = Objects.requireNonNull(endTime, "endTime must not be null");
        if (!endTime.isAfter(startTime)) throw new IllegalArgumentException("end time must be after start time");
        this.price = Objects.requireNonNull(price, "price must not be null");
        this.status = ReservationStatus.PENDING;
        this.paymentStatus = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Reservation(ReservationId id, UserId userId, CourtId courtId, LocalDate date, LocalTime startTime,
                       LocalTime endTime, BigDecimal price, ReservationStatus status, PaymentStatus paymentStatus,
                       LocalDateTime createdAt) {
        this(userId, courtId, date, startTime, endTime, price);
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.paymentStatus = Objects.requireNonNull(paymentStatus, "paymentStatus must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
    }

    public boolean overlaps(LocalDate otherDate, LocalTime otherStart, LocalTime otherEnd) {
        return date.equals(otherDate) && startTime.isBefore(otherEnd) && endTime.isAfter(otherStart);
    }

    public void approvePayment() {
        this.paymentStatus = PaymentStatus.APPROVED;
        this.status = ReservationStatus.CONFIRMED;
    }
}
