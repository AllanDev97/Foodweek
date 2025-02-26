package com.foodweek.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "MySuperSecretKeyForJWTGenerationAndSecurity12345"; // Min 32 caractères
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000; // 24 heures

    //  Utiliser `SecretKey` explicitement
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes()); //  Retourne bien un `SecretKey`
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), Jwts.SIG.HS256) //  Signature correcte avec `SecretKey`
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey()) // Accepte bien `SecretKey`
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // ✅ Plus d'erreur avec `SecretKey`
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
