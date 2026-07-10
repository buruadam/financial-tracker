package com.buruadam.financialtracker.service;

import com.buruadam.financialtracker.dto.UserLoginRequest;
import com.buruadam.financialtracker.dto.UserRegisterRequest;
import com.buruadam.financialtracker.dto.UserResponse;
import com.buruadam.financialtracker.entity.User;
import com.buruadam.financialtracker.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email is already registered");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(request.password())
                .build();

        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());
    }

    public UserResponse loginUser(UserLoginRequest request) {
        User user = userRepository.findByEmail(request.usernameOrEmail())
                .or(() -> userRepository.findByUsername(request.usernameOrEmail()))
                .orElseThrow(() -> new RuntimeException("Invalid username, email or password"));

        if (!user.getPassword().equals(request.password())) {
            throw new RuntimeException("Invalid username, email or password");
        }

        return new UserResponse(user.getId(), user.getUsername(), user.getEmail());
    }
}
