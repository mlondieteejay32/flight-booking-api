CREATE TABLE flights (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         flight_number VARCHAR(10) NOT NULL,
                         origin VARCHAR(3) NOT NULL,
                         destination VARCHAR(3) NOT NULL,
                         departure_time DATETIME NOT NULL,
                         arrival_time DATETIME NOT NULL,
                         price DECIMAL(10,2) NOT NULL,
                         CONSTRAINT uq_flight UNIQUE (flight_number, departure_time)
);

CREATE TABLE seats (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       flight_id BIGINT NOT NULL,
                       seat_number VARCHAR(4) NOT NULL,
                       booked BOOLEAN NOT NULL DEFAULT FALSE,
                       version BIGINT NOT NULL DEFAULT 0,
                       CONSTRAINT fk_seat_flight FOREIGN KEY (flight_id) REFERENCES flights (id),
                       CONSTRAINT uq_seat UNIQUE (flight_id, seat_number)
);

CREATE TABLE passengers (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            first_name VARCHAR(100) NOT NULL,
                            last_name VARCHAR(100) NOT NULL,
                            email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE bookings (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          reference VARCHAR(12) NOT NULL UNIQUE,
                          passenger_id BIGINT NOT NULL,
                          seat_id BIGINT NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          created_at DATETIME NOT NULL,
                          CONSTRAINT fk_booking_passenger FOREIGN KEY (passenger_id) REFERENCES passengers (id),
                          CONSTRAINT fk_booking_seat FOREIGN KEY (seat_id) REFERENCES seats (id)
);