package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.auth.LoginRequest;
import com.buruadam.financialtracker.dto.auth.RegisterRequest;
import com.buruadam.financialtracker.dto.auth.UserResponseDto;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.exception.ResourceAlreadyExistsException;
import com.buruadam.financialtracker.mapper.UserMapper;
import com.buruadam.financialtracker.repository.UserRepository;
import com.buruadam.financialtracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserResponseDto register(RegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username is already taken");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email is already registered");
        }

        User user = userMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDto(savedUser);
    }

    @Transactional(readOnly = true)
    public String login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
        );

        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new IllegalStateException("Authentication principal is not a UserDetails");
        }

        return jwtService.generateToken(userDetails);
    }
}
