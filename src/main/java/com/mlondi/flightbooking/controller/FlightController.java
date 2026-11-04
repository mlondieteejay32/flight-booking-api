package com.mlondi.flightbooking.controller;

import com.mlondi.flightbooking.dto.FlightResponse;
import com.mlondi.flightbooking.dto.SeatResponse;
import com.mlondi.flightbooking.service.FlightService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public List<FlightResponse> search(@RequestParam String origin,
                                       @RequestParam String destination,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return flightService.search(origin, destination, date);
    }

    @GetMapping("/{id}/seats")
    public List<SeatResponse> availableSeats(@PathVariable Long id) {
        return flightService.availableSeats(id);
    }
}
