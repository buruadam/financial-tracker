package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.UserLoginRequest;
import com.buruadam.financialtracker.dto.UserRegisterRequest;
import com.buruadam.financialtracker.dto.UserResponse;
import com.buruadam.financialtracker.security.CookieService;
import com.buruadam.financialtracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CookieService cookieService;

    public AuthController(AuthService authService, CookieService cookieService) {
        this.authService = authService;
        this.cookieService = cookieService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequest request) {
        String token = authService.login(request);
        ResponseCookie cookie = cookieService.createJwtCookie(token);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cookie = cookieService.cleanJwtCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }
}
