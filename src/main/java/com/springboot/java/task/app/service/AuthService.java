package com.springboot.java.task.app.service;

import com.springboot.java.task.app.auth.LoginRequest;
import com.springboot.java.task.app.auth.LoginResponse;
import com.springboot.java.task.app.auth.RegisterRequest;
import com.springboot.java.task.app.model.Role;
import com.springboot.java.task.app.model.User;
import com.springboot.java.task.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );
            var user = userRepository.findByUsername(req.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            // Generate JWT token
            var jwtToken = jwtService.generateToken(user);
            return LoginResponse.builder().access_token(jwtToken).token_type("Bearer ").build();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    public void register(RegisterRequest req) {
        String username = req.getUsername();
        String password = req.getPassword();
        String email = req.getEmail();
        validateUsername(username);
        validatePassword(password);
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }
        if (userRepository.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        validateUsername(username);
        var user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();
        userRepository.save(user);
    }

    public void validateUsername(String username) {
        if (username.length() < 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be at least 5 characters long");
        }
        if (username.length() > 30) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be at most 30 characters long");
        }
        if (!username.matches("^[a-zA-Z0-9_]*$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must contain only letters, numbers and underscores");
        }
    }

    public void validatePassword(String password) {
        if (password.length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long");
        }
        if (!password.matches(".*[a-zA-Z].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least one letter");
        }
        if (!password.matches(".*[0-9].*")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least one digit");
        }
    }
}
