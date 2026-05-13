package com.streetLeague.backend.dto.auth;

import com.streetLeague.backend.entity.User;

public record UserResponse(
        Integer id,
        String email,
        String nom,
        String prenom,
        String role,
        boolean emailVerified
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getIdUser(),
                user.getEmail(),
                user.getNom(),
                user.getPrenom(),
                user.getRole().name(),
                Boolean.TRUE.equals(user.getEmailVerified())
        );
    }
}
