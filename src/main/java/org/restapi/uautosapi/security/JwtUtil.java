package org.restapi.uautosapi.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.restapi.uautosapi.model.Customer;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.security.Key;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "mQ5Xz6qHn9Y2sVrjP7eLfTcX4WzUb8AxCvN7Jp3gKwLuYhEvGtRbQsDdNmAaXkZc";
    private final long EXPIRATION_TIME = 1000L * 60 * 60 * 24 * 30; // 30 días

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getEmail()) // sub
                .claim("userId", customer.getId())
                .claim("name", customer.getName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
