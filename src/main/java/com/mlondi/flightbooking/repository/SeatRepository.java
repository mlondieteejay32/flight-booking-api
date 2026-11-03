package com.mlondi.flightbooking.repository;

import com.mlondi.flightbooking.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByFlightIdAndBookedFalseOrderBySeatNumber(Long flightId);
}
