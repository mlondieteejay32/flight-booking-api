package com.mlondi.flightbooking.repository;

import com.mlondi.flightbooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByReference(String reference);

    List<Booking> findByPassengerEmailOrderByCreatedAtDesc(String email);
}
