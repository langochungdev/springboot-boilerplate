package com.instar.feature.chat.service;
import com.instar.feature.chat.dto.ChatDto;
import java.util.List;
import java.util.UUID;

public interface ChatService {
    ChatDto createGroup(String chatName, UUID creatorId, List<UUID> memberIds);
    ChatDto createPrivateChat(UUID userId1, UUID userId2);
    List<ChatDto> getUserChats(UUID userId);
    void addUserToGroup(UUID chatId, UUID userId, boolean isAdmin);
}
