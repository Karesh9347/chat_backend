package com.securechat.service;

import com.securechat.domain.Chat;
import com.securechat.domain.User;
import com.securechat.exception.ResourceNotFoundException;
import com.securechat.repository.ChatRepository;
import com.securechat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Chat createOrGetChat(UUID currentUserId, UUID otherUserId) {
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", currentUserId));

        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", otherUserId));

        // Check if chat already exists
        return chatRepository.findChatByParticipants(currentUser, otherUser)
                .orElseGet(() -> {
                    Chat chat = new Chat();
                    chat.setParticipant1(currentUser);
                    chat.setParticipant2(otherUser);
                    return chatRepository.save(chat);
                });
    }

    public List<Chat> getUserChats(UUID userId) {
        return chatRepository.findChatsByUserId(userId);
    }

    public Chat getChatById(UUID chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat", "id", chatId));
    }
}
