package com.streetLeague.backend.security;

public record JwtUserClaims(
        String email,
        String role,
        Integer userId,
        long expiresAt
) {
}
