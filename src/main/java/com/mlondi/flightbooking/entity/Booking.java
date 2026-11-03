package com.mlondi.flightbooking.entity;

import com.mlondi.flightbooking.exception.ConflictException;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(nullable = false)
    private BookingStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Booking() { }

    public Booking(String reference, Passenger passenger, Seat seat) {
        this.reference = reference;
        this.passenger = passenger;
        this.seat = seat;
        this.status = BookingStatus.CONFIRMED;
        this.createdAt = LocalDateTime.now();
    }

    public void cancel() {
        if (status == BookingStatus.CANCELLED) {
            throw new ConflictException("Booking " + reference + " is already cancelled");
        }
        status = BookingStatus.CANCELLED;
        seat.release();
    }

    public Long getId() { return id; }
    public String getReference() { return reference; }
    public Passenger getPassenger() { return passenger; }
    public Seat getSeat() { return seat; }
    public BookingStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
