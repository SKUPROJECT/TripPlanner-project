package com.example.tripplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TripPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TripPlannerApplication.class, args);
    }

}
