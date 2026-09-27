package org.shub.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.shub.userservice.entity.User;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {
    public static final String CLAIM_USERNAME = "name";
    public static final String CLAIM_EMAIL = "email";

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiry = LocalDateTime.now().plus(jwtProperties.getExpirationDays(), ChronoUnit.DAYS);

        var builder = Jwts.builder()
                .subject(user.getId().toString())
                .claim(CLAIM_USERNAME, user.getName())
                .claim(CLAIM_EMAIL, user.getEmail())
                .issuer(jwtProperties.getIssuer())
                .audience().add(jwtProperties.getAudience()).and()
                .issuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .expiration(Date.from(expiry.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(signingKey);

        return builder.compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(jwtProperties.getIssuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
