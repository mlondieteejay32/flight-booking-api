package com.mlondi.flightbooking.service;

import com.mlondi.flightbooking.dto.FlightResponse;
import com.mlondi.flightbooking.dto.SeatResponse;
import com.mlondi.flightbooking.exception.NotFoundException;
import com.mlondi.flightbooking.repository.FlightRepository;
import com.mlondi.flightbooking.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class FlightService {

    private final FlightRepository flightRepository;
    private final SeatRepository seatRepository;

    public FlightService(FlightRepository flightRepository, SeatRepository seatRepository) {
        this.flightRepository = flightRepository;
        this.seatRepository = seatRepository;
    }

    public List<FlightResponse> search(String origin, String destination, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        return flightRepository
                .search(origin.trim().toUpperCase(), destination.trim().toUpperCase(), start, start.plusDays(1))
                .stream()
                .map(FlightResponse::from)
                .toList();
    }

    public List<SeatResponse> availableSeats(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new NotFoundException("Flight " + flightId + " not found");
        }
        return seatRepository.findByFlightIdAndBookedFalseOrderBySeatNumber(flightId)
                .stream()
                .map(SeatResponse::from)
                .toList();
    }
}
