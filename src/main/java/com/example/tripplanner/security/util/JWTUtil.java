package com.example.tripplanner.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {
    private static final String key = "12345678901234567890123456789012"; /* Spring 에서 키 지정 */

    public String createToken(Map<String, Object> valueMap, int min) {
        SecretKey secretKey;

        try {
            secretKey = Keys.hmacShaKeyFor(key.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .addClaims(valueMap)
                .signWith(secretKey)
                .compact();
    }

    public Map<String, Object> validateToken(String token) {
        SecretKey secretKey;

        try {
            secretKey = Keys.hmacShaKeyFor(key.getBytes("UTF-8"));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        log.info("claims: " + claims);

        return claims;
    }
}
