package com.mlondi.flightbooking.entity;

import com.mlondi.flightbooking.exception.ConflictException;
import jakarta.persistence.*;

@Entity
@Table(name = "seats")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(nullable = false)
    private boolean booked;

    @Version
    private Long version;

    protected Seat() { }

    public void reserve() {
        if (booked) {
            throw new ConflictException("Seat " + seatNumber + " is already booked");
        }
        booked = true;
    }

    public void release() {
        booked = false;
    }

    public Long getId() { return id; }
    public Flight getFlight() { return flight; }
    public String getSeatNumber() { return seatNumber; }
    public boolean isBooked() { return booked; }
}
