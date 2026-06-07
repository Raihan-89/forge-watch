package com.forgewatch.alert_service.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@Getter
public class TwilioConfig {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.verify.service-sid}")
    private String verifyServiceSid;

    @Value("${twilio.from-phone-number:}")
    private String fromPhoneNumber;

    @Value("${twilio.messaging-service-sid:}")
    private String messagingServiceSid;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        log.info("Twilio initialized successfully");
    }
}
