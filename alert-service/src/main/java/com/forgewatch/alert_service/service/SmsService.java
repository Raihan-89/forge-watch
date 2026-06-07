package com.forgewatch.alert_service.service;

import com.forgewatch.alert_service.config.TwilioConfig;
import com.forgewatch.alert_service.dto.DefectAlertDto;
import com.forgewatch.alert_service.dto.MachineAlertDto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.type.PhoneNumber;
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
            Message message = sendAlertMessage(phoneNumber, buildMachineBreakdownMessage(machine));

            if (message == null) {
                log.error("Machine breakdown SMS not sent for {}. Twilio Verify only sends OTP messages; configure twilio.from-phone-number or twilio.messaging-service-sid for custom alerts.",
                        machine.getMachineCode());
                return;
            }

            log.info("Machine breakdown SMS sent for: {} messageSid: {}",
                    machine.getMachineCode(),
                    message.getSid());

        } catch (Exception e) {
            log.error("Failed to send machine breakdown SMS: {}", e.getMessage());
        }
    }

    public void sendDefectAlertSms(DefectAlertDto defect, String phoneNumber) {

        try {
            Message message = sendAlertMessage(phoneNumber, buildDefectAlertMessage(defect));

            if (message == null) {
                log.error("Defect alert SMS not sent for machine {}. Twilio Verify only sends OTP messages; configure twilio.from-phone-number or twilio.messaging-service-sid for custom alerts.",
                        defect.getMachineCode());
                return;
            }

            log.info("Defect alert SMS sent for machine: {} messageSid: {}",
                    defect.getMachineCode(),
                    message.getSid());

        } catch (Exception e) {
            log.error("Failed to send defect alert SMS: {}", e.getMessage());
        }
    }

    private Message sendAlertMessage(String phoneNumber, String body) {
        if (hasText(twilioConfig.getMessagingServiceSid())) {
            return Message.creator(
                    new PhoneNumber(phoneNumber),
                    twilioConfig.getMessagingServiceSid(),
                    body
            ).create();
        }

        if (hasText(twilioConfig.getFromPhoneNumber())) {
            return Message.creator(
                    new PhoneNumber(phoneNumber),
                    new PhoneNumber(twilioConfig.getFromPhoneNumber()),
                    body
            ).create();
        }

        return null;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String buildMachineBreakdownMessage(MachineAlertDto machine) {
        return """
                ForgeWatch machine breakdown alert
                Machine: %s
                Name: %s
                Department: %s
                Location: %s
                Status: %s
                Time: %s
                """.formatted(
                machine.getMachineCode(),
                machine.getMachineName(),
                machine.getDepartment(),
                machine.getLocation(),
                machine.getStatus(),
                machine.getUpdatedAt()
        );
    }

    private String buildDefectAlertMessage(DefectAlertDto defect) {
        return """
                ForgeWatch defect alert
                Machine: %s
                Severity: %s
                Title: %s
                Department: %s
                Reported by: %s
                Time: %s
                """.formatted(
                defect.getMachineCode(),
                defect.getSeverity(),
                defect.getTitle(),
                defect.getDepartment(),
                defect.getReportedByEmail(),
                defect.getReportedAt()
        );
    }
}
