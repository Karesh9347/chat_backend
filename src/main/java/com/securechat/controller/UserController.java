package com.securechat.controller;

import com.securechat.domain.User;
import com.securechat.repository.UserRepository;
import com.securechat.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam(required = false) String query,
            java.security.Principal principal) {
        String currentUsername = principal.getName();
        List<User> users;

        if (query == null || query.trim().isEmpty()) {
            users = userRepository.findAll();
        } else {
            users = userRepository.findByUsernameContaining(query);
        }

        // Filter out current user and clear sensitive data
        List<User> filteredUsers = users.stream()
                .filter(user -> !user.getUsername().equals(currentUsername))
                .peek(user -> user.setPasswordHash(null))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredUsers);
    }

    @GetMapping("/{userId}/public-key")
    public ResponseEntity<?> getPublicKey(@PathVariable UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return ResponseEntity.ok(new java.util.HashMap<String, String>() {
            {
                put("publicKey", user.getPublicKey());
            }
        });
    }
}
