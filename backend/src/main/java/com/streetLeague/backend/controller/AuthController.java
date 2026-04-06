package com.streetLeague.backend.controller;

import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email and password required"));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getMotDePasse())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
        }

        String token = java.util.Base64.getEncoder()
                .encodeToString((email + ":" + password).getBytes());

        return ResponseEntity.ok(Map.of(
                "id",       user.getIdUser(),
                "email",    user.getEmail(),
                "nom",      user.getNom(),
                "prenom",   user.getPrenom(),
                "role",     user.getRole().name(),
                "token",    token
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email    = body.get("email");
        String password = body.get("password");
        String nom      = body.get("nom");
        String prenom   = body.get("prenom");
        String roleStr  = body.get("role");

        if (email == null || password == null || nom == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email, password and nom are required"));
        }

        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "Email already in use"));
        }

        Role role;
        try {
            role = roleStr != null ? Role.valueOf(roleStr.toUpperCase()) : Role.JOUEUR;
        } catch (IllegalArgumentException e) {
            role = Role.JOUEUR;
        }

        User user = User.builder()
                .nom(nom)
                .prenom(prenom != null ? prenom : "")
                .email(email)
                .motDePasse(passwordEncoder.encode(password))
                .role(role)
                .build();

        userRepository.save(user);

        String token = java.util.Base64.getEncoder()
                .encodeToString((email + ":" + password).getBytes());

        return ResponseEntity.status(201).body(Map.of(
                "id",       user.getIdUser(),
                "email",    user.getEmail(),
                "nom",      user.getNom(),
                "prenom",   user.getPrenom(),
                "role",     user.getRole().name(),
                "token",    token
        ));
    }
}
