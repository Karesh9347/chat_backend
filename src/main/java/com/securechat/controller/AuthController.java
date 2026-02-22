package com.securechat.controller;

import com.securechat.domain.User;
import com.securechat.dto.ApiResponse;
import com.securechat.dto.LoginRequest;
import com.securechat.dto.JwtAuthenticationResponse;
import com.securechat.dto.RegisterRequest;
import com.securechat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest signUpRequest) {
        System.out.println("register user");
        try {
            User user = authService.registerUser(signUpRequest);

            LoginRequest loginRequest = new LoginRequest();
            loginRequest.setUsername(signUpRequest.getUsername());
            loginRequest.setPassword(signUpRequest.getPassword());

            JwtAuthenticationResponse response = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }
}
