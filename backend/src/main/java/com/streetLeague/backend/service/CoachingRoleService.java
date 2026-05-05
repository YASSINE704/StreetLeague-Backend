package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CoachingRoleService {

    private final UserRepository userRepository;

    /**
     * Vérifie que l'utilisateur est COACH ou ADMIN.
     * Lance BusinessRuleException sinon.
     */
    public User requireCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.COACH && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException(
                    "Accès refusé : seuls les COACH et ADMIN peuvent accéder à cette ressource");
        }
        return user;
    }

    /**
     * Vérifie que l'utilisateur est SPORTIF, COACH ou ADMIN.
     * Utilisé pour le suivi et le feedback.
     */
    public User requireSportifOrCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.SPORTIF
                && user.getRole() != Role.COACH
                && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException(
                    "Accès refusé : seuls les SPORTIF, COACH et ADMIN peuvent accéder à cette ressource");
        }
        return user;
    }

    /**
     * Recherche un utilisateur par ID ou lance ResourceNotFoundException.
     */
    public User findUserOrThrow(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec id: " + userId));
    }
}
