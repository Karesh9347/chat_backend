package com.securechat.service;

import com.securechat.domain.Chat;
import com.securechat.domain.Message;
import com.securechat.dto.MessageDTO;
import com.securechat.exception.ResourceNotFoundException;
import com.securechat.repository.ChatRepository;
import com.securechat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Transactional
    public Message saveMessage(UUID chatId, Message message) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Chat", "id", chatId));

        message.setChat(chat);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public Page<MessageDTO> getChatMessages(UUID chatId, Pageable pageable) {
        return messageRepository.findByChatId(chatId, pageable)
            .map(m -> new MessageDTO(
                m.getId(),
                m.getChat().getId(),
                m.getSenderId(),
                m.getContent(),
                m.getMediaUrl(),
                m.getMessageType() != null  // use 'm', not 'message'
                    ? m.getMessageType().name()
                    : Message.MessageType.TEXT.name(),
                m.getTimestamp()
            ));
    }
}
