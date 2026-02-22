package com.securechat.domain;

import jakarta.persistence.*;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "chats", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "participant1_id", "participant2_id" })
})
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "participant1_id", nullable = false)
    private User participant1;

    @ManyToOne
    @JoinColumn(name = "participant2_id", nullable = false)
    private User participant2;

    private LocalDateTime createdAt = LocalDateTime.now();

    public Chat() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getParticipant1() {
        return participant1;
    }

    public void setParticipant1(User participant1) {
        this.participant1 = participant1;
    }

    public User getParticipant2() {
        return participant2;
    }

    public void setParticipant2(User participant2) {
        this.participant2 = participant2;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper to check if a user is part of this chat
    public boolean isParticipant(UUID userId) {
        return participant1.getId().equals(userId) || participant2.getId().equals(userId);
    }
}
