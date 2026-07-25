package com.forgewatch.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI 3.0 configuration for API documentation.
 * Accessible at: http://localhost:{port}/swagger-ui.html
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:forgewatch}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI forgeWatchOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " - API Documentation")
                        .description("""
                                ForgeWatch is a production-ready factory floor monitoring system
                                built with Spring Boot microservices architecture.
                                
                                Features:
                                - Machine registration and status tracking
                                - Production shift planning and monitoring
                                - Defect reporting and resolution workflow
                                - Real-time alerts via email and SMS
                                - Role-based access control (ADMIN, SUPERVISOR, WORKER)
                                - Event-driven architecture with RabbitMQ
                                - OTP verification and password reset
                                """)
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Md. Raihan Shikder")
                                .email("raihan.shikder84@gmail.com")
                                .url("https://github.com/Raihan-89"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("ForgeWatch Documentation")
                        .url("https://github.com/Raihan-89/forge-watch"))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Gateway URL - Main API Entry Point"),
                        new Server()
                                .url("http://localhost:8081")
                                .description("Auth Service (Direct)"),
                        new Server()
                                .url("http://localhost:8082")
                                .description("Machine Service (Direct)"),
                        new Server()
                                .url("http://localhost:8083")
                                .description("Shift Service (Direct)"),
                        new Server()
                                .url("http://localhost:8084")
                                .description("Defect Service (Direct)"),
                        new Server()
                                .url("http://localhost:8085")
                                .description("Alert Service (Direct)")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createSecurityScheme()));
    }

    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name("Bearer Authentication")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Provide a valid JWT token. " +
                        "Obtain it by logging in through the auth service.");
    }
}
