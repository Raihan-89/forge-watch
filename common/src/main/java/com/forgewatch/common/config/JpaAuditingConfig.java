package com.forgewatch.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

/**
 * Enables JPA auditing across all services.
 * Automatically populates createdAt, updatedAt, createdBy, updatedBy fields.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            try {
                ServletRequestAttributes requestAttributes =
                        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    String userEmail = requestAttributes.getRequest()
                            .getHeader("X-User-Email");
                    if (userEmail != null && !userEmail.isBlank()) {
                        return Optional.of(userEmail);
                    }
                }
            } catch (Exception ignored) {
                // If there's no request context (e.g., background jobs), use system
            }
            return Optional.of("SYSTEM");
        };
    }
}
