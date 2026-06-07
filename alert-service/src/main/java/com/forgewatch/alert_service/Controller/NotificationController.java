package com.forgewatch.alert_service.controller;

import com.forgewatch.alert_service.dto.ForgotPasswordRequest;
import com.forgewatch.alert_service.dto.OtpRequest;
import com.forgewatch.alert_service.dto.OtpVerifyRequest;
import com.forgewatch.alert_service.dto.ResetPasswordRequest;
import com.forgewatch.alert_service.service.PasswordResetService;
import com.forgewatch.alert_service.service.SmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final PasswordResetService passwordResetService;

    private final SmsService smsService;

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        passwordResetService.processForgotPassword(request.getEmail());

        return ResponseEntity.ok(
                "Password reset email sent to: " + request.getEmail()
        );
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(
            @RequestBody ResetPasswordRequest request) {

        String email = passwordResetService.validateResetToken(request.getToken());

        return ResponseEntity.ok("Token valid for email: " + email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        String email = passwordResetService.validateResetToken(request.getToken());

        passwordResetService.deleteResetToken(request.getToken());

        return ResponseEntity.ok(
                "Password reset successful for: " + email
        );
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(
            @Valid @RequestBody OtpRequest request) {

        smsService.sendOtp(request.getPhoneNumber());

        return ResponseEntity.ok(
                "OTP sent to: " + request.getPhoneNumber()
        );
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @Valid @RequestBody OtpVerifyRequest request) {

        boolean verified = smsService.verifyOtp(
                request.getPhoneNumber(),
                request.getOtpCode()
        );

        if (verified) {
            return ResponseEntity.ok("OTP verified successfully");
        }

        return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }
}