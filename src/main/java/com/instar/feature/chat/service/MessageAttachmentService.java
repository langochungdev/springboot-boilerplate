package com.instar.feature.chat.service;
import com.instar.feature.chat.dto.MessageAttachmentDto;

import java.util.List;

public interface MessageAttachmentService {
    MessageAttachmentDto add(MessageAttachmentDto dto);
    List<MessageAttachmentDto> findByMessageId(String messageId);
    void delete(String id);
}
