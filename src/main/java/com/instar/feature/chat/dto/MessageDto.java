package com.instar.feature.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
    private String id;
    private String chatId;
    private String senderId;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private List<MessageAttachmentDto> attachments;
}
