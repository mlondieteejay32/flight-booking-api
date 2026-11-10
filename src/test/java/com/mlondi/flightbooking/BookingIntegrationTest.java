package com.mlondi.flightbooking;

import com.mlondi.flightbooking.dto.CreateBookingRequest;
import com.mlondi.flightbooking.dto.FlightResponse;
import com.mlondi.flightbooking.exception.ConflictException;
import com.mlondi.flightbooking.repository.SeatRepository;
import com.mlondi.flightbooking.service.BookingService;
import com.mlondi.flightbooking.service.FlightService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.OptimisticLockingFailureException;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(properties = "DB_PASSWORD=unused")
class BookingIntegrationTest {

    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:8.4");

    @Autowired
    private FlightService flightService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private SeatRepository seatRepository;

    @Test
    void searchFindsTheSampleFlight() {
        List<FlightResponse> flights = flightService.search("JNB", "CPT", LocalDate.of(2027, 3, 10));

        assertThat(flights).extracting(FlightResponse::flightNumber).containsExactly("FB101");
    }

    @Test
    void whenManyPeopleBookTheSameSeat_onlyOneSucceeds() throws Exception {
        Long seatId = seatRepository.findByFlightIdAndBookedFalseOrderBySeatNumber(1L).get(0).getId();
        int attempts = 10;
        ExecutorService executor = Executors.newFixedThreadPool(attempts);
        CountDownLatch ready = new CountDownLatch(attempts);
        CountDownLatch go = new CountDownLatch(1);
        List<Future<Boolean>> outcomes = new ArrayList<>();

        for (int i = 0; i < attempts; i++) {
            int number = i;
            outcomes.add(executor.submit(() -> {
                ready.countDown();
                go.await();
                try {
                    bookingService.createBooking(new CreateBookingRequest(
                            seatId, "Test", "User" + number, "user" + number + "@example.com"));
                    return true;
                } catch (ConflictException | OptimisticLockingFailureException e) {
                    return false;
                }
            }));
        }

        ready.await();
        go.countDown();

        int successes = 0;
        for (Future<Boolean> outcome : outcomes) {
            if (outcome.get()) {
                successes++;
            }
        }
        executor.shutdown();

        assertThat(successes).isEqualTo(1);
        assertThat(seatRepository.findById(seatId).orElseThrow().isBooked()).isTrue();
    }
}
