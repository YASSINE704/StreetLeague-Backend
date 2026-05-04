package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.auth.AuthResponse;
import com.streetLeague.backend.dto.auth.ForgotPasswordRequest;
import com.streetLeague.backend.dto.auth.LoginRequest;
import com.streetLeague.backend.dto.auth.RegisterRequest;
import com.streetLeague.backend.dto.auth.ResendVerificationRequest;
import com.streetLeague.backend.dto.auth.ResetPasswordRequest;
import com.streetLeague.backend.dto.auth.UserResponse;
import com.streetLeague.backend.dto.auth.VerifyEmailRequest;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Niveau;
import com.streetLeague.backend.enums.Position;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.exception.AuthException;
import com.streetLeague.backend.repository.JoueurRepository;
import com.streetLeague.backend.repository.UserRepository;
import com.streetLeague.backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 15;
    private static final int CODE_EXPIRATION_MINUTES = 10;
    private static final Set<Role> REGISTRATION_ROLES = Set.of(Role.JOUEUR, Role.COACH, Role.SPORTIF);

    private final UserRepository userRepository;
    private final JoueurRepository joueurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final SecureRandom secureRandom = new SecureRandom();

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String email = normalizeEmail(request.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> invalidCredentials(email));

        if (isLocked(user)) {
            log.warn("Blocked login attempt for locked account [{}]", email);
            throw new AuthException(HttpStatus.TOO_MANY_REQUESTS,
                    "Too many failed login attempts. Please try again later.");
        }

        if (!passwordEncoder.matches(request.password(), user.getMotDePasse())) {
            recordFailedAttempt(user);
            log.warn("Failed login attempt for [{}]", email);
            throw invalidCredentials(email);
        }

        if (!isEmailVerified(user)) {
            log.warn("Unverified account login attempt for [{}]", email);
            throw new AuthException(HttpStatus.FORBIDDEN, "Email must be verified before sign in.");
        }

        clearFailedAttempts(user);
        userRepository.save(user);

        log.info("Successful login for [{}] with role [{}]", email, user.getRole());
        return new AuthResponse(
                jwtService.generateToken(user),
                "Bearer",
                jwtService.getExpirationSeconds(),
                UserResponse.from(user)
        );
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new AuthException(HttpStatus.CONFLICT, "Email is already in use.");
        }

        Role role = parseRegistrationRole(request.role());
        String code = generateCode();
        User user = User.builder()
                .nom(request.nom())
                .prenom(request.prenom())
                .email(email)
                .motDePasse(passwordEncoder.encode(request.password()))
                .role(role)
                .emailVerified(false)
                .emailVerificationCode(code)
                .emailVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES))
                .failedLoginAttempts(0)
                .build();

        userRepository.save(user);
        if (role == Role.JOUEUR) {
            createPlayerProfile(request, user);
        }
        emailService.sendVerificationCode(email, code);

        log.info("Registered new [{}] account for [{}]. Verification required.", role, email);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse verifyEmail(VerifyEmailRequest request) {
        String email = normalizeEmail(request.email());
        User user = findUser(email);

        if (isEmailVerified(user)) {
            return UserResponse.from(user);
        }

        if (!isCodeValid(request.code(), user.getEmailVerificationCode(), user.getEmailVerificationCodeExpiresAt())) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Verification code is invalid or expired.");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationCode(null);
        user.setEmailVerificationCodeExpiresAt(null);
        userRepository.save(user);

        log.info("Verified email for [{}]", email);
        return UserResponse.from(user);
    }

    @Transactional
    public void resendVerificationCode(ResendVerificationRequest request) {
        String email = normalizeEmail(request.email());
        User user = findUser(email);

        if (isEmailVerified(user)) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Email is already verified.");
        }

        String code = generateCode();
        user.setEmailVerificationCode(code);
        user.setEmailVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));
        userRepository.save(user);
        emailService.sendVerificationCode(email, code);

        log.info("Resent verification code for [{}]", email);
    }

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        String email = normalizeEmail(request.email());
        userRepository.findByEmail(email).ifPresent(user -> {
            String code = generateCode();
            user.setResetPasswordCode(code);
            user.setResetPasswordCodeExpiresAt(LocalDateTime.now().plusMinutes(CODE_EXPIRATION_MINUTES));
            userRepository.save(user);
            emailService.sendPasswordResetCode(email, code);
            log.info("Issued password reset code for [{}]", email);
        });
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        String email = normalizeEmail(request.email());
        User user = findUser(email);

        if (!isCodeValid(request.code(), user.getResetPasswordCode(), user.getResetPasswordCodeExpiresAt())) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Reset code is invalid or expired.");
        }

        user.setMotDePasse(passwordEncoder.encode(request.newPassword()));
        user.setResetPasswordCode(null);
        user.setResetPasswordCodeExpiresAt(null);
        clearFailedAttempts(user);
        userRepository.save(user);

        log.info("Password reset completed for [{}]", email);
    }

    private AuthException invalidCredentials(String email) {
        log.warn("Invalid credentials for [{}]", email);
        return new AuthException(HttpStatus.UNAUTHORIZED, "Invalid email or password.");
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(HttpStatus.NOT_FOUND, "User not found."));
    }

    private Role parseRegistrationRole(String value) {
        String normalizedRole = value.trim().toUpperCase(Locale.ROOT)
                .replace("PLAYER", "JOUEUR")
                .replace(" ", "_");

        Role role;
        try {
            role = Role.valueOf(normalizedRole);
        } catch (IllegalArgumentException ex) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Unsupported role for registration.");
        }

        if (!REGISTRATION_ROLES.contains(role)) {
            throw new AuthException(HttpStatus.FORBIDDEN,
                    "Only Player, Coach and Sportif accounts can sign up.");
        }
        return role;
    }

    private void createPlayerProfile(RegisterRequest request, User user) {
        if (request.age() == null || request.age() < 6 || request.age() > 100) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Player age must be between 6 and 100.");
        }

        Niveau niveau = parseEnum(request.niveau(), Niveau.class, "Player level is required.");
        Position position = parseEnum(request.position(), Position.class, "Player position is required.");
        if (request.profilePicture() == null || request.profilePicture().isBlank()) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Player profile picture is required.");
        }

        Joueur joueur = new Joueur();
        joueur.setNom((request.prenom() + " " + request.nom()).trim());
        joueur.setAge(request.age());
        joueur.setNiveau(niveau);
        joueur.setPosition(position);
        joueur.setProfilePicture(request.profilePicture());
        joueur.setUser(user);

        joueurRepository.save(joueur);
        log.info("Created linked joueur profile [{}] for user [{}]", joueur.getId(), user.getEmail());
    }

    private <E extends Enum<E>> E parseEnum(String value, Class<E> enumType, String missingMessage) {
        if (value == null || value.isBlank()) {
            throw new AuthException(HttpStatus.BAD_REQUEST, missingMessage);
        }

        try {
            return Enum.valueOf(enumType, value.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new AuthException(HttpStatus.BAD_REQUEST, "Invalid " + enumType.getSimpleName() + " value.");
        }
    }

    private String generateCode() {
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }

    private boolean isCodeValid(String submittedCode, String expectedCode, LocalDateTime expiresAt) {
        return expectedCode != null
                && expiresAt != null
                && LocalDateTime.now().isBefore(expiresAt)
                && expectedCode.equals(submittedCode);
    }

    private boolean isLocked(User user) {
        return user.getAccountLockedUntil() != null
                && LocalDateTime.now().isBefore(user.getAccountLockedUntil());
    }

    private void recordFailedAttempt(User user) {
        int attempts = user.getFailedLoginAttempts() == null ? 1 : user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(attempts);
        if (attempts >= MAX_FAILED_ATTEMPTS) {
            user.setAccountLockedUntil(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
        }
        userRepository.save(user);
    }

    private void clearFailedAttempts(User user) {
        user.setFailedLoginAttempts(0);
        user.setAccountLockedUntil(null);
    }

    private boolean isEmailVerified(User user) {
        return user.getEmailVerified() == null || Boolean.TRUE.equals(user.getEmailVerified());
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
