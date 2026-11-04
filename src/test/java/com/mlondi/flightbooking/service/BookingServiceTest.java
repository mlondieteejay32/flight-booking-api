package com.mlondi.flightbooking.service;

import com.mlondi.flightbooking.dto.BookingResponse;
import com.mlondi.flightbooking.dto.CreateBookingRequest;
import com.mlondi.flightbooking.entity.Booking;
import com.mlondi.flightbooking.entity.BookingStatus;
import com.mlondi.flightbooking.entity.Passenger;
import com.mlondi.flightbooking.entity.Seat;
import com.mlondi.flightbooking.entity.TestData;
import com.mlondi.flightbooking.exception.ConflictException;
import com.mlondi.flightbooking.exception.NotFoundException;
import com.mlondi.flightbooking.repository.BookingRepository;
import com.mlondi.flightbooking.repository.PassengerRepository;
import com.mlondi.flightbooking.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    private static final CreateBookingRequest REQUEST =
            new CreateBookingRequest(10L, "Thandi", "Nkosi", "Thandi@Example.com");

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void createBooking_reservesSeatAndConfirmsBooking() {
        Seat seat = TestData.seat(false);
        when(seatRepository.findById(10L)).thenReturn(Optional.of(seat));
        when(passengerRepository.findByEmail("thandi@example.com")).thenReturn(Optional.empty());
        when(passengerRepository.save(any(Passenger.class))).thenAnswer(inv -> inv.getArgument(0));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        BookingResponse response = bookingService.createBooking(REQUEST);

        assertThat(response.status()).isEqualTo(BookingStatus.CONFIRMED);
        assertThat(response.seatNumber()).isEqualTo("1A");
        assertThat(response.passengerName()).isEqualTo("Thandi Nkosi");
        assertThat(response.reference()).hasSize(8);
        assertThat(seat.isBooked()).isTrue();
    }

    @Test
    void createBooking_whenSeatAlreadyBooked_throwsConflictAndSavesNothing() {
        when(seatRepository.findById(10L)).thenReturn(Optional.of(TestData.seat(true)));

        assertThatThrownBy(() -> bookingService.createBooking(REQUEST))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already booked");

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void createBooking_whenSeatDoesNotExist_throwsNotFound() {
        when(seatRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.createBooking(REQUEST))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void cancelBooking_cancelsBookingAndFreesSeat() {
        Seat seat = TestData.seat(true);
        when(bookingRepository.findByReference("ABC12345")).thenReturn(Optional.of(confirmedBooking(seat)));

        BookingResponse response = bookingService.cancelBooking("abc12345");

        assertThat(response.status()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(seat.isBooked()).isFalse();
    }

    @Test
    void cancelBooking_whenAlreadyCancelled_throwsConflict() {
        Booking booking = confirmedBooking(TestData.seat(true));
        booking.cancel();
        when(bookingRepository.findByReference("ABC12345")).thenReturn(Optional.of(booking));

        assertThatThrownBy(() -> bookingService.cancelBooking("ABC12345"))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("already cancelled");
    }

    @Test
    void cancelBooking_whenReferenceUnknown_throwsNotFound() {
        when(bookingRepository.findByReference("NOPE1234")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingService.cancelBooking("NOPE1234"))
                .isInstanceOf(NotFoundException.class);
    }

    private static Booking confirmedBooking(Seat seat) {
        Passenger passenger = new Passenger("Thandi", "Nkosi", "thandi@example.com");
        return new Booking("ABC12345", passenger, seat);
    }
}