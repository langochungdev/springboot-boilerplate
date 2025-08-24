package com.boilerplate.feature.chat.service;
import com.boilerplate.feature.chat.dto.MessageAttachmentDto;

import java.util.List;
import java.util.UUID;

public interface MessageAttachmentService {
    MessageAttachmentDto add(MessageAttachmentDto dto);
    List<MessageAttachmentDto> findByMessageId(UUID messageId);
    void delete(UUID id);
}
