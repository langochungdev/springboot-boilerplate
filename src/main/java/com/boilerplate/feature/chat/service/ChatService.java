package com.boilerplate.feature.chat.service;
import com.boilerplate.feature.chat.dto.MessageDto;

public interface ChatService {
    void sendPrivateMessage(MessageDto dto);
    void sendGroupMessage(MessageDto dto);
}
