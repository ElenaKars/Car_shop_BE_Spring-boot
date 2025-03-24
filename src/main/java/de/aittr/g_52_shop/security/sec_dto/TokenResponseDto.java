package de.aittr.g_52_shop.security.sec_dto;

import java.util.Objects;

public class TokenResponseDto {

    private String accessToken;
    private String refreshToken;

    public TokenResponseDto() {
    }

    public TokenResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TokenResponseDto that = (TokenResponseDto) o;
        return Objects.equals(accessToken, that.accessToken) && Objects.equals(refreshToken, that.refreshToken);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accessToken);
        result = 31 * result + Objects.hashCode(refreshToken);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Token Response DTO: access token -%s, refresh token - %s", accessToken, refreshToken);
    }
}
