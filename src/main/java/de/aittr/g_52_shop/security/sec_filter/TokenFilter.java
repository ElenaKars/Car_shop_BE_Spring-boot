package de.aittr.g_52_shop.security.sec_filter;

import de.aittr.g_52_shop.domain.entity.User;
import de.aittr.g_52_shop.security.AuthInfo;
import de.aittr.g_52_shop.security.sec_service.TokenService;
import de.aittr.g_52_shop.service.interfaces.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
public class TokenFilter extends GenericFilterBean {
    private final TokenService service;
    public TokenFilter(TokenService service) {
        this.service = service;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = getTokenFromRequest((HttpServletRequest) servletRequest);

        if (token != null && service.validateAccessToken(token)) {
            Claims claims = service.getAccessClaims(token);
            AuthInfo authInfo = service.mapClaimsToAuthInfo(claims);
            authInfo.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(authInfo);
        }
        filterChain.doFilter(servletRequest, servletResponse);
            }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        // Bearer hjg4gh2g4h2jh4y2gnbl2kj4324

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }
}
