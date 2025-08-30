package com.boilerplate.feature.chat.repository;

import com.boilerplate.feature.chat.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatUserRepository extends JpaRepository<ChatUser, UUID> {
}
