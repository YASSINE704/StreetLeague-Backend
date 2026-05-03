package com.streetLeague.backend.service;

import com.streetLeague.backend.entity.User;
import com.streetLeague.backend.enums.Role;
import com.streetLeague.backend.exception.BusinessRuleException;
import com.streetLeague.backend.exception.ResourceNotFoundException;
import com.streetLeague.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service utilitaire pour la vérification des rôles dans le module Coaching.
 * Centralise la logique d'autorisation pour éviter la duplication dans chaque service.
 *
 * Rôles coaching :
 * - ADMIN  : accès complet
 * - COACH  : créer/modifier/supprimer programmes, séances, exercices
 * - SPORTIF: consulter (GET), ajouter un suivi, rejoindre une séance
 */
@Service
@RequiredArgsConstructor
public class CoachingRoleService {

    private final UserRepository userRepository;

    /**
     * Récupère un utilisateur par son ID ou lève une exception.
     */
    public User findUserOrThrow(Integer userId) {
        if (userId == null) {
            throw new BusinessRuleException("L'identifiant utilisateur est requis (header X-User-Id)");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé avec id: " + userId));
    }

    /**
     * Vérifie que l'utilisateur a le rôle COACH ou ADMIN.
     * Utilisé pour les opérations de création/modification/suppression de programmes, séances, exercices.
     */
    public User requireCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.COACH && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException(
                    "Accès refusé : seuls les coachs et administrateurs peuvent effectuer cette action. Votre rôle : " + user.getRole());
        }
        return user;
    }

    /**
     * Vérifie que l'utilisateur a le rôle SPORTIF, COACH ou ADMIN.
     * Utilisé pour les opérations de suivi (feedback après séance).
     */
    public User requireSportifOrCoachOrAdmin(Integer userId) {
        User user = findUserOrThrow(userId);
        if (user.getRole() != Role.SPORTIF && user.getRole() != Role.COACH && user.getRole() != Role.ADMIN) {
            throw new BusinessRuleException(
                    "Accès refusé : seuls les sportifs, coachs et administrateurs peuvent effectuer cette action. Votre rôle : " + user.getRole());
        }
        return user;
    }

    /**
     * Vérifie que l'utilisateur est authentifié (n'importe quel rôle).
     * Utilisé pour les opérations de lecture (GET).
     */
    public User requireAuthenticated(Integer userId) {
        return findUserOrThrow(userId);
    }
}
