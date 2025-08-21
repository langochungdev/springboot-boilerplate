package com.instar.feature.chat.service;


import com.instar.feature.chat.dto.MessageDto;

import java.util.List;

public interface MessageService {
    List<MessageDto> getConversations(String userId);
    MessageDto save(MessageDto dto);
    void markRead(String messageId);
}
