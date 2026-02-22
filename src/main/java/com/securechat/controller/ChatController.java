package com.securechat.controller;

import com.securechat.domain.Chat;
import com.securechat.domain.User;
import com.securechat.service.ChatService;
import com.securechat.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    // In a real app, I'd fetch User entity from UserDetails cleanly.
    // For now, I'll rely on looking up ID from username in UserDetails if needed
    // But ChatService expects UUID.
    // Let's assume the frontend sends the other user's UUID to start a chat.

    // Helper to get current user ID (Quick & Dirty for this scope, ideally in a
    // base controller or utility)
    private UUID getCurrentUserId(UserDetails userDetails) {
        // This is inefficient, but trying to keep it simple.
        // Ideally UserDetails implementation has the ID.
        // Let's cast custom UserDetails if I implemented it, but I used
        // org.springframework.security.core.userdetails.User
        // So I have to look it up.
        return ((com.securechat.domain.User) ((com.securechat.service.CustomUserDetailsService) userDetailsService)
                .loadUserByUsername(userDetails.getUsername())).getId();

        // Wait, CustomUserDetailsService returns Spring User, not Domain User.
        // I should injected UserRepository here or modify CustomUserDetailsService to
        // return custom implementation.
        // modifying CustomUserDetailsService is better but for now let's query repo.
    }

    // Actually, let's just inject UserRepository to lookup ID.
    @Autowired
    private com.securechat.repository.UserRepository userRepository;

    private UUID getUserId(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow().getId();
    }

    @PostMapping
    public ResponseEntity<Chat> createChat(@AuthenticationPrincipal UserDetails currentUser,
            @RequestParam UUID tempPartnerId) { // Changed param name to avoid confusion
        // Wait, normally we start chat by username or ID.
        // Let's accept partnerId in body or param.
        return ResponseEntity.ok(chatService.createOrGetChat(getUserId(currentUser), tempPartnerId));
    }

    // Better endpoint: Start chat by username
    @PostMapping("/verify-user")
    public ResponseEntity<?> getChatByUsername(@AuthenticationPrincipal UserDetails currentUser,
            @RequestParam String username) {
        User otherUser = userRepository.findByUsername(username)
                .orElseThrow(
                        () -> new com.securechat.exception.ResourceNotFoundException("User", "username", username));

        return ResponseEntity.ok(chatService.createOrGetChat(getUserId(currentUser), otherUser.getId()));
    }

    @GetMapping
    public ResponseEntity<List<Chat>> getMyChats(@AuthenticationPrincipal UserDetails currentUser) {
        return ResponseEntity.ok(chatService.getUserChats(getUserId(currentUser)));
    }
}
