package com.streetLeague.backend.entity;

import com.streetLeague.backend.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUser;

    private String nom;
    private String prenom;

    @Column(unique = true)
    private String email;

    private String motDePasse;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder.Default
    private Boolean emailVerified = true;

    private String emailVerificationCode;
    private LocalDateTime emailVerificationCodeExpiresAt;

    @Builder.Default
    private Integer failedLoginAttempts = 0;

    private LocalDateTime accountLockedUntil;

    private String resetPasswordCode;
    private LocalDateTime resetPasswordCodeExpiresAt;
}
