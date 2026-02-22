package com.securechat.service;

import com.securechat.domain.User;
import com.securechat.dto.LoginRequest;
import com.securechat.dto.JwtAuthenticationResponse;
import com.securechat.dto.RegisterRequest;
import com.securechat.exception.ResourceNotFoundException;
import com.securechat.repository.UserRepository;
import com.securechat.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", loginRequest.getUsername()));

        return new JwtAuthenticationResponse(jwt, user);
    }

    public User registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setPasswordHash(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPublicKey(signUpRequest.getPublicKey());
        user.setRoles(Collections.singleton("USER"));

        return userRepository.save(user);
    }
}
