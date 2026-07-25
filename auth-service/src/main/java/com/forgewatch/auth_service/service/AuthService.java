package com.forgewatch.auth_service.service;

import com.forgewatch.auth_service.dto.AuthResponse;
import com.forgewatch.auth_service.dto.LoginRequest;
import com.forgewatch.auth_service.dto.RegisterRequest;
import com.forgewatch.auth_service.entity.User;
import com.forgewatch.auth_service.enums.Role;
import com.forgewatch.auth_service.repository.UserRepository;
import com.forgewatch.auth_service.security.JwtUtil;
import com.forgewatch.common.exception.DuplicateResourceException;
import com.forgewatch.common.exception.InvalidOperationException;
import com.forgewatch.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for authentication and user management.
 * Handles registration, login, and password management.
 *
 * @author Md. Raihan Shikder (Raihan-89)
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .department(request.getDepartment())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.WORKER)
                .build();

        User saved = userRepository.save(user);

        log.info("User registered: {} (publicId: {})", saved.getEmail(), saved.getPublicId());

        String token = jwtUtil.generateToken(
                saved.getEmail(),
                saved.getRole().name(),
                saved.getDepartment()
        );

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(3600000)
                .publicId(saved.getPublicId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .role(saved.getRole().name())
                .department(saved.getDepartment())
                .build();
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidOperationException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name(),
                user.getDepartment()
        );

        log.info("User logged in: {} (publicId: {})", user.getEmail(), user.getPublicId());

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresIn(3600000)
                .publicId(user.getPublicId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .department(user.getDepartment())
                .build();
    }
}
