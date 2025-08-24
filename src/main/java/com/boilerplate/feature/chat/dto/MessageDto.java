package com.boilerplate.feature.chat.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private UUID id;
    private UUID chatId;
    private UUID senderId;
    private UUID receiverId; // chỉ dùng cho private chat
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private List<MessageAttachmentDto> attachments;
}
