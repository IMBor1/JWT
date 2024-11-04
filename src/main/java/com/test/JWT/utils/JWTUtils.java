package com.test.JWT.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtils {
    @Value("${security.token}")
    private final SecretKey secretKey;
    private static final long EXPIRATION_TIME = 86400000; // 24 часа

    public JWTUtils() {
        // Создание секретного ключа из строки
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("your-secret-key"));
    }

    // Метод для генерации JWT токена на основе данных пользователя
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // Метод для извлечения имени пользователя из токена
    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    // Метод для извлечения ролей из токена
    public Object extractRoles(String token) {
        return extractClaims(token, claims -> claims.get("roles"));
    }

    // Общий метод для извлечения данных из токена
    private <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Метод для извлечения всех данных (claims) из токена
    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SignatureException e) {
            throw new RuntimeException("Invalid JWT signature");
        }
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}