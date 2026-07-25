package com.forgewatch.alert_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Alert Service - Handles email/SMS notifications, OTP verification, and password reset.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@SpringBootApplication(scanBasePackages = {"com.forgewatch"})
public class AlertServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlertServiceApplication.class, args);
	}

}
