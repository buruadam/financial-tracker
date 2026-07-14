package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.UserLoginRequest;
import com.buruadam.financialtracker.dto.UserRegisterRequest;
import com.buruadam.financialtracker.dto.UserResponse;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.exception.ResourceAlreadyExistsException;
import com.buruadam.financialtracker.repository.UserRepository;
import com.buruadam.financialtracker.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserResponse register(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new ResourceAlreadyExistsException("Username is already taken");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new ResourceAlreadyExistsException("Email is already registered");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), null);
    }

    @Transactional(readOnly = true)
    public String login(UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.usernameOrEmail(), request.password())
        );

        if (!(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new IllegalStateException("Authentication principal is not a UserDetails");
        }

        return jwtService.generateToken(userDetails);
    }
}
