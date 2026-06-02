package com.forgewatch.auth_service.service;

import com.forgewatch.auth_service.dto.AuthResponse;
import com.forgewatch.auth_service.dto.LoginRequest;
import com.forgewatch.auth_service.dto.RegisterRequest;
import com.forgewatch.auth_service.entity.User;
import com.forgewatch.auth_service.enums.Role;
import com.forgewatch.auth_service.repository.UserRepository;
import com.forgewatch.auth_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .role(Role.WORKER)
                .build();

        userRepository.save(user);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getDepartment()
        );

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name(),
                user.getDepartment()
        );
    }

    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getDepartment()
        );

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name(),
                user.getDepartment()
        );
    }
}
