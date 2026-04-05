package com.streetLeague.backend.config;

import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class SecurityDataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDefaultUsers() {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            userRepository.save(User.builder()
                    .nom("Admin")
                    .prenom("System")
                    .email("admin@streetleague.tn")
                    .motDePasse(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build());

            userRepository.save(User.builder()
                    .nom("Coach")
                    .prenom("Demo")
                    .email("coach@streetleague.tn")
                    .motDePasse(passwordEncoder.encode("coach123"))
                    .role(Role.COACH)
                    .build());

            userRepository.save(User.builder()
                    .nom("Sportif")
                    .prenom("Demo")
                    .email("sportif@streetleague.tn")
                    .motDePasse(passwordEncoder.encode("sportif123"))
                    .role(Role.SPORTIF)
                    .build());
        };
    }
}
