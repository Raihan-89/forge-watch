package com.forgewatch.alert_service.service;

import com.forgewatch.alert_service.config.TwilioConfig;
import com.forgewatch.alert_service.dto.DefectAlertDto;
import com.forgewatch.alert_service.dto.MachineAlertDto;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmsService {

    private final TwilioConfig twilioConfig;

    public void sendOtp(String phoneNumber) {

        try {
            Verification verification = Verification.creator(
                    twilioConfig.getVerifyServiceSid(),
                    phoneNumber,
                    "sms"
            ).create();

            log.info("OTP sent to: {} status: {}",
                    phoneNumber,
                    verification.getStatus());

        } catch (Exception e) {
            log.error("Failed to send OTP: {}", e.getMessage());
            throw new RuntimeException("Failed to send OTP: " + e.getMessage());
        }
    }

    public boolean verifyOtp(String phoneNumber, String otpCode) {

        try {
            VerificationCheck check = VerificationCheck.creator(
                            twilioConfig.getVerifyServiceSid()
                    )
                    .setTo(phoneNumber)
                    .setCode(otpCode)
                    .create();

            boolean approved = "approved".equals(check.getStatus());

            log.info("OTP verification for: {} result: {}",
                    phoneNumber,
                    check.getStatus());

            return approved;

        } catch (Exception e) {
            log.error("Failed to verify OTP: {}", e.getMessage());
            return false;
        }
    }

    public void sendMachineBreakdownSms(MachineAlertDto machine, String phoneNumber) {

        try {
            Verification.creator(
                    twilioConfig.getVerifyServiceSid(),
                    phoneNumber,
                    "sms"
            ).create();

            log.info("Machine breakdown SMS sent for: {}", machine.getMachineCode());

        } catch (Exception e) {
            log.error("Failed to send machine breakdown SMS: {}", e.getMessage());
        }
    }

    public void sendDefectAlertSms(DefectAlertDto defect, String phoneNumber) {

        try {
            Verification.creator(
                    twilioConfig.getVerifyServiceSid(),
                    phoneNumber,
                    "sms"
            ).create();

            log.info("Defect alert SMS sent for machine: {}", defect.getMachineCode());

        } catch (Exception e) {
            log.error("Failed to send defect alert SMS: {}", e.getMessage());
        }
    }
}