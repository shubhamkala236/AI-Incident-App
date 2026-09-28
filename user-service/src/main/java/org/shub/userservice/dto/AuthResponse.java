package org.shub.userservice.dto;

public record AuthResponse(
        String token,
        String username,
        Long userId
) {
}
