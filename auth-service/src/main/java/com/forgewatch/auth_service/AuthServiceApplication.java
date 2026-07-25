package com.forgewatch.auth_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Auth Service - Handles user registration, login, and JWT token management.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@SpringBootApplication(scanBasePackages = {"com.forgewatch"})
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
