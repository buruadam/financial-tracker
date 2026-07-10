package com.buruadam.financialtracker.controller;

import com.buruadam.financialtracker.dto.UserLoginRequest;
import com.buruadam.financialtracker.dto.UserRegisterRequest;
import com.buruadam.financialtracker.dto.UserResponse;
import com.buruadam.financialtracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody UserRegisterRequest request) {
        UserResponse response = userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserResponse response = userService.loginUser(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
