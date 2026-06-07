package com.forgewatch.alert_service.service;

import com.forgewatch.alert_service.dto.DefectAlertDto;
import com.forgewatch.alert_service.dto.MachineAlertDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${notification.supervisor.email}")
    private String supervisorEmail;

    public void sendMachineBreakdownEmail(MachineAlertDto machine) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(supervisorEmail);
            message.setSubject(
                    "🚨 URGENT - Machine Breakdown: " + machine.getMachineCode()
            );
            message.setText(buildMachineAlertBody(machine));

            mailSender.send(message);

            log.info("Machine breakdown email sent for: {}", machine.getMachineCode());

        } catch (Exception e) {
            log.error("Failed to send machine breakdown email: {}", e.getMessage());
        }
    }

    public void sendDefectAlertEmail(DefectAlertDto defect) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(supervisorEmail);
            message.setSubject(
                    "⚠️ " + defect.getSeverity() + " Defect Alert - Machine: "
                            + defect.getMachineCode()
            );
            message.setText(buildDefectAlertBody(defect));

            mailSender.send(message);

            log.info("Defect alert email sent for machine: {}", defect.getMachineCode());

        } catch (Exception e) {
            log.error("Failed to send defect alert email: {}", e.getMessage());
        }
    }

    public void sendPasswordResetEmail(String toEmail, String resetToken) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("ForgeWatch - Password Reset Request");
            message.setText(buildPasswordResetBody(resetToken));

            mailSender.send(message);

            log.info("Password reset email sent to: {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage());
        }
    }

    private String buildMachineAlertBody(MachineAlertDto machine) {
        return """
                MACHINE BREAKDOWN ALERT
                =======================
                Machine Code  : %s
                Machine Name  : %s
                Department    : %s
                Location      : %s
                Status        : %s
                Time          : %s
                
                Please take immediate action.
                
                ForgeWatch Monitoring System
                """.formatted(
                machine.getMachineCode(),
                machine.getMachineName(),
                machine.getDepartment(),
                machine.getLocation(),
                machine.getStatus(),
                machine.getUpdatedAt()
        );
    }

    private String buildDefectAlertBody(DefectAlertDto defect) {
        return """
                DEFECT ALERT NOTIFICATION
                =========================
                Defect ID     : %s
                Machine Code  : %s
                Department    : %s
                Title         : %s
                Description   : %s
                Severity      : %s
                Reported By   : %s
                Time          : %s
                
                Please investigate immediately.
                
                ForgeWatch Monitoring System
                """.formatted(
                defect.getId(),
                defect.getMachineCode(),
                defect.getDepartment(),
                defect.getTitle(),
                defect.getDescription(),
                defect.getSeverity(),
                defect.getReportedByEmail(),
                defect.getReportedAt()
        );
    }

    private String buildPasswordResetBody(String resetToken) {
        return """
                Password Reset Request
                ======================
                You requested a password reset for your ForgeWatch account.
                
                Your reset token is:
                
                %s
                
                This token will expire in 15 minutes.
                
                If you did not request this, please ignore this email.
                
                ForgeWatch Monitoring System
                """.formatted(resetToken);
    }
}