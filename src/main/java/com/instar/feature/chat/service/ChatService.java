package com.instar.feature.chat.service;
import com.instar.feature.chat.dto.ChatDto;
import com.instar.feature.chat.dto.MessageDto;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    void sendPrivateMessage(MessageDto dto);
    void sendGroupMessage(MessageDto dto);
}
