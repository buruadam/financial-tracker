package com.buruadam.financialtracker.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${app.cookie.name}")
    private String cookieName;

    public ResponseCookie createJwtCookie(String token) {
        long maxAgeSeconds = jwtExpirationMs / 1000;

        return ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(maxAgeSeconds)
                .sameSite("Lax")
                .build();
    }

    public ResponseCookie cleanJwtCookie() {
        return ResponseCookie.from(cookieName, "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
    }
}
