package com.mlondi.flightbooking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI flightBookingOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Flight Booking API")
                .version("1.0")
                .description("Search flights, view free seats, book and cancel. A seat can never be double booked."));
    }
}