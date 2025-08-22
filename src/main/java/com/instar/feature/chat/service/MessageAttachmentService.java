package com.instar.feature.chat.service;
import com.instar.feature.chat.dto.MessageAttachmentDto;

import java.util.List;
import java.util.UUID;

public interface MessageAttachmentService {
    MessageAttachmentDto add(MessageAttachmentDto dto);
    List<MessageAttachmentDto> findByMessageId(UUID messageId);
    void delete(UUID id);
}
