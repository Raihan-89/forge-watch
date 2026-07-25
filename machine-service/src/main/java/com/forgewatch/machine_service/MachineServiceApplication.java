package com.forgewatch.machine_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Machine Service - Handles machine registration, status tracking, and breakdown events.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@SpringBootApplication(scanBasePackages = {"com.forgewatch"})
public class MachineServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MachineServiceApplication.class, args);
	}

}
