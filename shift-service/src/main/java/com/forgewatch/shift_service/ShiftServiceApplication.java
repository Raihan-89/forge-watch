package com.forgewatch.shift_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Shift Service - Handles shift planning, worker assignments, and production tracking.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@SpringBootApplication(scanBasePackages = {"com.forgewatch"})
public class ShiftServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShiftServiceApplication.class, args);
	}

}
