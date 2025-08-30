package com.boilerplate.feature.chat.service;
import com.boilerplate.feature.chat.dto.MessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto save(MessageDto dto);
}
