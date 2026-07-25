package com.forgewatch.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Authentication response DTO containing JWT token and user details.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Data
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

    private String tokenType;

    private long expiresIn;

    private String publicId;

    private String fullName;

    private String email;

    private String role;

    private String department;

    public AuthResponse(String token, String email, String role, String department) {
        this.token = token;
        this.tokenType = "Bearer";
        this.expiresIn = 3600000; // 1 hour in milliseconds
        this.email = email;
        this.role = role;
        this.department = department;
    }
}
