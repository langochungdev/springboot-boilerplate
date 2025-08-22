package com.instar.feature.chat.repository;

import com.instar.feature.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
