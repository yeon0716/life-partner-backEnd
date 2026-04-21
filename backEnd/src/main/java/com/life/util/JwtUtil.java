package com.life.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private static final long ACCESS_EXP = 1000 * 60 * 60; // 1시간
    private static final long REFRESH_EXP = 1000L * 60 * 60 * 24 * 7; // 7일

    /* =========================
        TOKEN 생성
    ========================= */

    public String createAccessToken(String email, Long memberId) {
        return Jwts.builder()
                .setSubject(email)
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    /* =========================
        PARSE 공통 메서드 (핵심)
    ========================= */

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /* =========================
        GETTERS
    ========================= */

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    public Long getMemberId(String token) {
        Object value = parseClaims(token).get("memberId");

        if (value == null) return null;

        return Long.valueOf(value.toString());
    }

    /* =========================
        VALIDATION
    ========================= */

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT expired");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT");
        } catch (MalformedJwtException e) {
            System.out.println("Malformed JWT");
        } catch (SecurityException e) {
            System.out.println("Invalid signature");
        } catch (Exception e) {
            System.out.println("Invalid JWT");
        }
        return false;
    }
}