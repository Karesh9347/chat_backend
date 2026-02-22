package com.securechat.repository;

import com.securechat.domain.Chat;
import com.securechat.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {

    @Query("SELECT c FROM Chat c WHERE (c.participant1 = :user1 AND c.participant2 = :user2) OR (c.participant1 = :user2 AND c.participant2 = :user1)")
    Optional<Chat> findChatByParticipants(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT c FROM Chat c WHERE c.participant1.id = :userId OR c.participant2.id = :userId")
    List<Chat> findChatsByUserId(@Param("userId") UUID userId);
}
