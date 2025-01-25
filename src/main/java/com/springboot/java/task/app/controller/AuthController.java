package com.springboot.java.task.app.controller;

import com.springboot.java.task.app.auth.LoginRequest;
import com.springboot.java.task.app.auth.LoginResponse;
import com.springboot.java.task.app.auth.RegisterRequest;
import com.springboot.java.task.app.service.AuthService;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login/")
    public ResponseEntity<LoginResponse> login(@RequestParam("username") String username,
                                               @RequestParam("password") String password) {
        LoginRequest req = new LoginRequest(username, password);
        return ResponseEntity.ok(authService.login(req));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register/")
    public ResponseEntity<String> register(@RequestParam("username") String username,
                                           @RequestParam("email") @Email String email,
                                           @RequestParam("password") String password) {
        RegisterRequest req = new RegisterRequest(username, email, password);
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/auth/")
    public ResponseEntity<String> auth() {
        return ResponseEntity.ok("Authenticated");
    }
}
