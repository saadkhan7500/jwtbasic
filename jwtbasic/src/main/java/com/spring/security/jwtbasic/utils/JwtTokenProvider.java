package com.spring.security.jwtbasic.utils;

import java.security.Key;
import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
    private final Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public ResponseEntity<String> generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // 1 hour

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();

        return ResponseEntity.ok(token);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}