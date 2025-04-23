package com.mirs.auction.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String base64Secret;

    @Value("${jwt.expiration-ms}")
    private long jwtExpirationMs;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Décode la chaîne Base64 en bytes puis crée la clé HS512
        byte[] keyBytes = java.util.Base64.getDecoder().decode(base64Secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Génère un JWT pour le sujet donné (ici : userName).
     */
    public String generateToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Parse & valide le JWT, retourne les claims si OK.
     * Lance une exception sinon.
     */
    public Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
    }
}

