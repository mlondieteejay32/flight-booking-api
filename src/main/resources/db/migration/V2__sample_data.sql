INSERT INTO flights (flight_number, origin, destination, departure_time, arrival_time, price) VALUES
                                                                                                  ('FB101', 'JNB', 'CPT', '2027-03-10 08:00:00', '2027-03-10 10:05:00', 1499.00),
                                                                                                  ('FB102', 'CPT', 'JNB', '2027-03-10 12:00:00', '2027-03-10 14:05:00', 1399.00),
                                                                                                  ('FB201', 'JNB', 'DUR', '2027-03-11 07:30:00', '2027-03-11 08:50:00', 999.00);

INSERT INTO seats (flight_id, seat_number)
SELECT f.id, CONCAT(r.row_no, s.letter)
FROM flights f
         CROSS JOIN (SELECT 1 AS row_no UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) r
         CROSS JOIN (SELECT 'A' AS letter UNION SELECT 'B' UNION SELECT 'C' UNION SELECT 'D') s;