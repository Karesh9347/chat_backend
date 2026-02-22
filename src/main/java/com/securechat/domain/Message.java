package com.securechat.domain;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages", indexes = {
        @Index(name = "idx_chat_timestamp", columnList = "chat_id, timestamp DESC")
})
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    @JsonIgnoreProperties({ "participant1", "participant2", "createdAt" })
    private Chat chat;

    @Column(nullable = false)
    private UUID senderId;

    @Column(columnDefinition = "TEXT")
    private String content; // Client-side Encrypted content

    private String mediaUrl; // Optional: URL to stored media (also encrypted/secure)

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private LocalDateTime timestamp = LocalDateTime.now();

    public enum MessageType {
        TEXT, IMAGE, VIDEO
    }

    public Message() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
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

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
