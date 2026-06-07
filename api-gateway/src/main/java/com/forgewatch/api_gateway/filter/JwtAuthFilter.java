package com.forgewatch.api_gateway.filter;

import com.forgewatch.api_gateway.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;

    private final List<String> openEndpoints = List.of(
            "/api/auth/register",
            "/api/auth/login",
            "/api/notifications/forgot-password",
            "/api/notifications/validate-token",
            "/api/notifications/reset-password",
            "/api/notifications/send-otp",
            "/api/notifications/verify-otp"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        if (isOpenEndpoint(path)) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.isValid(token)) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-Email", jwtUtil.extractEmail(token))
                        .header("X-User-Role", jwtUtil.extractRole(token))
                        .header("X-User-Department", jwtUtil.extractDepartment(token))
                        .build())
                .build();

        return chain.filter(mutatedExchange);
    }

    private boolean isOpenEndpoint(String path) {
        return openEndpoints.stream().anyMatch(path::equals);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}