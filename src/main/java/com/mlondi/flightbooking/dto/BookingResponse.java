package com.mlondi.flightbooking.dto;

import com.mlondi.flightbooking.entity.Booking;
import com.mlondi.flightbooking.entity.BookingStatus;
import com.mlondi.flightbooking.entity.Flight;
import com.mlondi.flightbooking.entity.Passenger;
import com.mlondi.flightbooking.entity.Seat;

import java.time.LocalDateTime;

public record BookingResponse(String reference, BookingStatus status, String flightNumber,
                              String origin, String destination, LocalDateTime departureTime,
                              String seatNumber, String passengerName, LocalDateTime createdAt) {

    public static BookingResponse from(Booking booking) {
        Seat seat = booking.getSeat();
        Flight flight = seat.getFlight();
        Passenger passenger = booking.getPassenger();
        return new BookingResponse(booking.getReference(), booking.getStatus(), flight.getFlightNumber(),
                flight.getOrigin(), flight.getDestination(), flight.getDepartureTime(),
                seat.getSeatNumber(), passenger.getFirstName() + " " + passenger.getLastName(),
                booking.getCreatedAt());
    }
}