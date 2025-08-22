package com.instar.feature.chat.service;


import com.instar.feature.chat.dto.MessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<MessageDto> getConversations(UUID chatId);
    MessageDto save(MessageDto dto);
    void markRead(UUID messageId);
}
