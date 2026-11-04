package com.mlondi.flightbooking.service;

import com.mlondi.flightbooking.dto.BookingResponse;
import com.mlondi.flightbooking.dto.CreateBookingRequest;
import com.mlondi.flightbooking.entity.Booking;
import com.mlondi.flightbooking.entity.Passenger;
import com.mlondi.flightbooking.entity.Seat;
import com.mlondi.flightbooking.exception.NotFoundException;
import com.mlondi.flightbooking.repository.BookingRepository;
import com.mlondi.flightbooking.repository.PassengerRepository;
import com.mlondi.flightbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final SeatRepository seatRepository;

    public BookingService(BookingRepository bookingRepository,
                          PassengerRepository passengerRepository,
                          SeatRepository seatRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {
        Seat seat = seatRepository.findById(request.seatId())
                .orElseThrow(() -> new NotFoundException("Seat " + request.seatId() + " not found"));
        seat.reserve();

        String email = request.email().trim().toLowerCase();
        Passenger passenger = passengerRepository.findByEmail(email)
                .orElseGet(() -> passengerRepository.save(
                        new Passenger(request.firstName().trim(), request.lastName().trim(), email)));

        Booking booking = bookingRepository.save(new Booking(newReference(), passenger, seat));
        return BookingResponse.from(booking);
    }

    @Transactional
    public BookingResponse cancelBooking(String reference) {
        Booking booking = bookingRepository.findByReference(reference.trim().toUpperCase())
                .orElseThrow(() -> new NotFoundException("Booking " + reference + " not found"));
        booking.cancel();
        return BookingResponse.from(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> listBookings(String email) {
        return bookingRepository.findByPassengerEmailOrderByCreatedAtDesc(email.trim().toLowerCase())
                .stream()
                .map(BookingResponse::from)
                .toList();
    }

    private String newReference() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
}
