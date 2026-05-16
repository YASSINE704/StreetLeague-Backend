package com.streetLeague.backend.security;

import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Résout l'utilisateur authentifié à partir du SecurityContext (JWT).
 * Remplace la dépendance au header X-User-Id qui n'est jamais envoyé par le frontend.
 */
@Component
@RequiredArgsConstructor
public class AuthenticatedUserResolver {

    private final UserRepository userRepository;

    /**
     * Retourne l'ID de l'utilisateur actuellement authentifié via JWT.
     * Retourne null si aucun utilisateur n'est authentifié.
     */
    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            // L'email est utilisé comme username dans CustomUserDetailsService
            Optional<User> user = userRepository.findByEmail(userDetails.getUsername());
            return user.map(User::getIdUser).orElse(null);
        }

        return null;
    }

    /**
     * Retourne l'ID utilisateur : priorité au header X-User-Id (pour tests/Postman),
     * sinon extraction depuis le JWT SecurityContext.
     */
    public Integer resolveUserId(Integer headerUserId) {
        if (headerUserId != null) {
            return headerUserId;
        }
        return getCurrentUserId();
    }
}
