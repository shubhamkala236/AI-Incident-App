package org.shub.userservice.security;

import io.jsonwebtoken.Claims;

import java.util.UUID;

public record AuthenticatedUser(String subject, Claims claims) {

    public Long userId() {
        return Long.parseLong(subject);
    }

    public String username() {
        return claims.get(JwtService.CLAIM_USERNAME, String.class);
    }

    public String email() {
        return claims.get(JwtService.CLAIM_EMAIL, String.class);
    }
}