package com.mlondi.flightbooking.repository;

import com.mlondi.flightbooking.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("select f from Flight f where f.origin = :origin and f.destination = :destination "
            + "and f.departureTime >= :start and f.departureTime < :end order by f.departureTime")
    List<Flight> search(@Param("origin") String origin,
                        @Param("destination") String destination,
                        @Param("start") LocalDateTime start,
                        @Param("end") LocalDateTime end);
}
