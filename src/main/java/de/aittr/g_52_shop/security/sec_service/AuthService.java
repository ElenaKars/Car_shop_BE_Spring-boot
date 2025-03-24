package de.aittr.g_52_shop.security.sec_service;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.security.sec_dto.TokenResponseDto;
import de.aittr.g_52_shop.service.interfaces.UserService;
import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final Map<String, String> refreshStorage;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserService userService, TokenService tokenService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.passwordEncoder = passwordEncoder;
        this.refreshStorage = new HashMap<>();
    }

    public TokenResponseDto login(User inboundUser) throws AuthException {
        String username = inboundUser.getUsername();
        UserDetails foundUser = userService.loadUserByUsername(username);

        if(passwordEncoder.matches(inboundUser.getPassword(), foundUser.getPassword())){
            String accessToken = tokenService.generateAccessToken(foundUser);
            String refreshToken = tokenService.generateRefreshToken(foundUser);
            refreshStorage.put(username, refreshToken);
            return new TokenResponseDto(accessToken, refreshToken);
        } else{
            throw new AuthException("Password is incorrect");
        }
    }
    public TokenResponseDto getNewAccessToken(String inboundRefreshToken){
        Claims refreshClaims = tokenService.getRefreshClaims(inboundRefreshToken);
        String username = refreshClaims.getSubject();
        String foundRefreshToken = refreshStorage.get(username);

        if(foundRefreshToken != null && foundRefreshToken.equals(inboundRefreshToken)){
            UserDetails foundUser = userService.loadUserByUsername(username);
            String accessToken = tokenService.generateAccessToken(foundUser);
            return new TokenResponseDto(accessToken);
        }else{
            return new TokenResponseDto(null);
        }
    }
}
