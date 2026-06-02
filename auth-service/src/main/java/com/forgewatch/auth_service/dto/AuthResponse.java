package com.forgewatch.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Data
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private String email;

    private String role;

    private String department;
}
