package com.boilerplate.feature.chat.service;
import com.boilerplate.feature.chat.dto.MessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<MessageDto> getConversations(UUID chatId);
    MessageDto save(MessageDto dto);
}
