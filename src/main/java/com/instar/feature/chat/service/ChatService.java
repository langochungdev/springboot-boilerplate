package com.instar.feature.chat.service;
import com.instar.feature.chat.dto.ChatDto;
import java.util.List;

public interface ChatService {
    ChatDto createGroup(String chatName, String creatorId, List<String> memberIds);
    List<ChatDto> getUserChats(String userId);
    void addUserToGroup(String chatId, String userId, boolean isAdmin);
}
