package com.instar.feature.chat.dto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ChatDto {
    private UUID id;
    private String chatName;
    private Boolean isGroup;
    private UUID createdById;
    private LocalDateTime createdAt;
    private List<UUID> memberIds;
}
