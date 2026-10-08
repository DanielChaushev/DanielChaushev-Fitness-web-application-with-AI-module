package com.danielchaushev.fitnessbackend.controller;

import com.danielchaushev.fitnessbackend.dto.AuthResponse;
import com.danielchaushev.fitnessbackend.dto.LoginRequest;
import com.danielchaushev.fitnessbackend.dto.RegisterRequest;
import com.danielchaushev.fitnessbackend.model.BodyWeightLog;
import com.danielchaushev.fitnessbackend.model.User;
import com.danielchaushev.fitnessbackend.repository.BodyWeightLogRepository;
import com.danielchaushev.fitnessbackend.repository.UserRepository;
import com.danielchaushev.fitnessbackend.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final BodyWeightLogRepository bodyWeightLogRepository;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil, BodyWeightLogRepository bodyWeightLogRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.bodyWeightLogRepository = bodyWeightLogRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Този имейл вече е регистриран."));
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setGender(request.getGender());
        user.setAge(request.getAge());
        user.setHeight(request.getHeight());
        user.setWeight(request.getWeight());
        user.setGoal(request.getGoal());
        user.setActivityLevel(request.getActivityLevel());
        userRepository.save(user);

        if (request.getWeight() != null) {
            BodyWeightLog initialLog = new BodyWeightLog();
            initialLog.setUser(user);
            initialLog.setDate(LocalDate.now());
            initialLog.setWeightKg(request.getWeight());
            bodyWeightLogRepository.save(initialLog);
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Грешен имейл или парола."));
        }

        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}