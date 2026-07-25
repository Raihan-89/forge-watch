package com.forgewatch.defect_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Defect Service - Handles defect reporting, tracking, and resolution workflow.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@SpringBootApplication(scanBasePackages = {"com.forgewatch"})
public class DefectServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DefectServiceApplication.class, args);
	}

}
