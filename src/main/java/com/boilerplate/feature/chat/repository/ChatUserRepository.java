package com.boilerplate.feature.chat.repository;

import com.boilerplate.feature.chat.entity.ChatUser;
import com.boilerplate.feature.chat.entity.ChatUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatUserRepository extends JpaRepository<ChatUser, ChatUserId> {
    List<ChatUser> findByUserId(UUID userId);
    List<ChatUser> findByChatId(UUID chatId);
}
