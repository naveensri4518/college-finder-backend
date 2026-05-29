package com.college.service;

import com.college.dto.LoginRequest;
import com.college.dto.RegisterRequest;
import com.college.model.User;
import com.college.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    /**
     * Register a new user.
     */
    public Map<String, Object> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // In production, use BCrypt password encoding.
        // For simplicity, storing plain password here.
        User user = new User(null, request.getName(), request.getEmail(), request.getPassword());
        User saved = userRepository.save(user);

        String token = generateToken(saved);
        return buildAuthResponse(saved, token);
    }

    /**
     * Login an existing user.
     */
    public Map<String, Object> login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = generateToken(user);
        return buildAuthResponse(user, token);
    }

    /**
     * Validate a token and return user ID.
     * Token format: Base64(userId:email)
     */
    public Optional<Long> validateToken(String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8);
            String[] parts = decoded.split(":");
            if (parts.length < 2) return Optional.empty();
            Long userId = Long.parseLong(parts[0]);
            // Verify user still exists
            if (userRepository.existsById(userId)) {
                return Optional.of(userId);
            }
        } catch (Exception e) {
            // Invalid token
        }
        return Optional.empty();
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private String generateToken(User user) {
        String raw = user.getId() + ":" + user.getEmail();
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }

    private Map<String, Object> buildAuthResponse(User user, String token) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("name", user.getName());
        response.put("email", user.getEmail());
        return response;
    }
}
