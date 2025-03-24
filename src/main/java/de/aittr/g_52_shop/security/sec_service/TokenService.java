package de.aittr.g_52_shop.security.sec_service;

import de.aittr.g_52_shop.domain.entity.Role;
import de.aittr.g_52_shop.repository.RoleRepository;
import de.aittr.g_52_shop.security.AuthInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class TokenService {
    //the storage of keys

    private SecretKey accessKey;
    private SecretKey refreshKey;
    private RoleRepository roleRepository;

    private TokenService(
            @Value("${key.access}") String accessPhrase,
            @Value("${key.refresh}") String refreshPhrase,
            RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
        this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessPhrase));
        this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshPhrase));
    }

    public String generateAccessToken(UserDetails user) {
        LocalDateTime currentDate = LocalDateTime.now();
        Instant expiration = currentDate.plusDays(1).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(accessKey)
                .claim("roles", user.getAuthorities())
                .claim("name", user.getUsername())
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        LocalDateTime currentDate = LocalDateTime.now();
        Instant expiration = currentDate.plusDays(7).atZone(ZoneId.systemDefault()).toInstant();
        Date expirationDate = Date.from(expiration);

        return Jwts.builder()
                .subject(user.getUsername())
                .expiration(expirationDate)
                .signWith(refreshKey)
                .compact();
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, accessKey);
    }

    public boolean validateRefreshToken(String refreshToken) {
        return validateToken(refreshToken, refreshKey);
    }

    public boolean validateToken(String token, SecretKey key) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getAccessClaims(String accessToken) {
        return getClaims(accessToken, accessKey);
    }

    public Claims getRefreshClaims(String refreshToken) {
        return getClaims(refreshToken, refreshKey);
    }

    public Claims getClaims(String token, SecretKey key) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public AuthInfo mapClaimsToAuthInfo(Claims claims) {
        String username = claims.getSubject();
        List<LinkedHashMap<String, String>> rolesList = (List<LinkedHashMap<String, String>>) claims.get("roles");
        Set<Role> roles = new HashSet<>();
        for (LinkedHashMap<String, String> roleEntry : rolesList) {
            String roleTitle = roleEntry.get("authority");
            roleRepository.findByTitle(roleTitle).ifPresent(roles::add);
        }
        return new AuthInfo(username, roles);
    }
}
