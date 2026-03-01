package com.pm.saaspossystem.configurations;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtProvider {

    SecretKey key = Keys.hmacShaKeyFor(
            JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8)
    );

    public String generateAccessToken(Authentication authentication) {

       Collection<? extends GrantedAuthority> authorites = authentication.getAuthorities();

       String roles = populateAuthorities(authorites);

       return  Jwts
               .builder()
               .claim("email",authentication.getName()) // email/username
               .claim("authorities", roles)
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + JwtConstant.EXPIRATION_TIME))
               .signWith(key)
               .compact();

    }
//[ROLE_USER, ROLE_ADMIN] --->"ROLE_USER,ROLE_ADMIN"
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }


    private Claims extractAllClaims(String token) {

        SecretKey key = Keys.hmacShaKeyFor(
                JwtConstant.SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmailFromToken(String token) {

        Claims claims = extractAllClaims(token);

        return claims.get("email", String.class);
    }

}
