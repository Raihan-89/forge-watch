package com.forgewatch.alert_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordResetService {

    private final RedisTemplate<String, String> redisTemplate;

    private final EmailService emailService;

    private static final String RESET_TOKEN_PREFIX = "password_reset:";

    private static final long TOKEN_EXPIRY_MINUTES = 15;

    public void processForgotPassword(String email) {

        String resetToken = UUID.randomUUID().toString();

        String redisKey = RESET_TOKEN_PREFIX + resetToken;

        redisTemplate.opsForValue().set(
                redisKey,
                email,
                TOKEN_EXPIRY_MINUTES,
                TimeUnit.MINUTES
        );

        emailService.sendPasswordResetEmail(email, resetToken);

        log.info("Password reset token generated for: {}", email);
    }

    public String validateResetToken(String token) {

        String redisKey = RESET_TOKEN_PREFIX + token;

        String email = redisTemplate.opsForValue().get(redisKey);

        if (email == null) {
            throw new RuntimeException("Invalid or expired reset token");
        }

        return email;
    }

    public void deleteResetToken(String token) {
        redisTemplate.delete(RESET_TOKEN_PREFIX + token);
    }
}