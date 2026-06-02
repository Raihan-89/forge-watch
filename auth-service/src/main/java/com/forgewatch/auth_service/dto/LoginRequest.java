package com.forgewatch.auth_service.dto;

import lombok.Data;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Data
public class LoginRequest {

    private String email;

    private String password;
}