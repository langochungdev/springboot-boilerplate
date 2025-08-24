package com.boilerplate.feature.chat.repository;
import com.boilerplate.feature.chat.dto.ChatDto;
import com.boilerplate.feature.chat.entity.Chat;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    @Query("select c from Chat c join c.chatUsers cu1 join c.chatUsers cu2 " +
            "where c.isGroup = false and cu1.user.id = :userId1 and cu2.user.id = :userId2")
    Optional<Chat> findPrivateChat(UUID userId1, UUID userId2);
    @Query("SELECT c FROM Chat c LEFT JOIN FETCH c.chatUsers WHERE c.id = :id")
    Optional<Chat> findByIdWithUsers(@Param("id") UUID id);
    @Query("SELECT c FROM Chat c JOIN c.chatUsers cu WHERE cu.user.id = :userId")
    List<Chat> findByUserId(UUID userId);
}
