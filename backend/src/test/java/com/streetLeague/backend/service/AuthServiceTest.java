package com.streetLeague.backend.service;

import com.streetLeague.backend.dto.auth.RegisterRequest;
import com.streetLeague.backend.dto.auth.VerifyEmailRequest;
import com.streetLeague.backend.entity.Joueur;
import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.exception.AuthException;
import com.streetLeague.backend.repository.JoueurRepository;
import com.streetLeague.backend.repository.UserRepository;
import com.streetLeague.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JoueurRepository joueurRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_whenValidPlayerRequest_shouldEncodePasswordSaveUserAndSendVerification() {
        RegisterRequest request = new RegisterRequest(
                "Alice",
                "Smith",
                "alice@example.com",
                "StrongPass123!",
                "JOUEUR",
                20,
                "INTERMEDIATE",
                "FORWARD",
                "avatar.png"
        );

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("StrongPass123!")).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(joueurRepository.save(any(Joueur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail()).isEqualTo("alice@example.com");
        assertThat(savedUser.getMotDePasse()).isEqualTo("encoded-password");
        assertThat(savedUser.getEmailVerificationCode()).isNotBlank().matches("\\d{6}");
        assertThat(savedUser.getEmailVerified()).isFalse();
        verify(passwordEncoder).encode("StrongPass123!");
        verify(joueurRepository).save(any(Joueur.class));
        verify(emailService).sendVerificationCode(eq("alice@example.com"), eq(savedUser.getEmailVerificationCode()));
    }

    @Test
    void register_whenEmailAlreadyExists_shouldThrowAndSkipSaveAndMail() {
        RegisterRequest request = new RegisterRequest(
                "Bob",
                "Martin",
                "bob@example.com",
                "StrongPass123!",
                "JOUEUR",
                22,
                "BEGINNER",
                "MIDFIELDER",
                "avatar.png"
        );

        when(userRepository.existsByEmail("bob@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("already in use");

        verify(userRepository, never()).save(any(User.class));
        verify(joueurRepository, never()).save(any(Joueur.class));
        verify(emailService, never()).sendVerificationCode(any(), any());
    }

    @Test
    void verifyEmail_whenCodeIsValid_shouldEnableAccountAndClearCode() {
        User user = User.builder()
                .email("carol@example.com")
                .role(Role.JOUEUR)
                .emailVerified(false)
                .emailVerificationCode("123456")
                .emailVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(10))
                .build();

        when(userRepository.findByEmail("carol@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.verifyEmail(new VerifyEmailRequest("carol@example.com", "123456"));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertThat(updatedUser.getEmailVerified()).isTrue();
        assertThat(updatedUser.getEmailVerificationCode()).isNull();
        assertThat(updatedUser.getEmailVerificationCodeExpiresAt()).isNull();
    }

    @Test
    void verifyEmail_whenCodeIsInvalidOrExpired_shouldThrowAndLeaveAccountDisabled() {
        User user = User.builder()
                .email("dave@example.com")
                .emailVerified(false)
                .emailVerificationCode("123456")
                .emailVerificationCodeExpiresAt(LocalDateTime.now().minusMinutes(1))
                .build();

        when(userRepository.findByEmail("dave@example.com")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.verifyEmail(new VerifyEmailRequest("dave@example.com", "999999")))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("invalid or expired");

        assertThat(user.getEmailVerified()).isFalse();
        assertThat(user.getEmailVerificationCode()).isEqualTo("123456");
        assertThat(user.getEmailVerificationCodeExpiresAt()).isNotNull();
        verify(userRepository, never()).save(any(User.class));
    }
}
