package com.securechat.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageDTO {

    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private String content;
    private String mediaUrl;
    private String messageType;
    private LocalDateTime timestamp;

    // Default constructor
    public MessageDTO() {
    }

    // All-args constructor
    public MessageDTO(UUID id, UUID chatId, UUID senderId, String content,
                      String mediaUrl, String messageType, LocalDateTime timestamp) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.messageType = messageType;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}