package com.boilerplate.feature.chat.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatDto {
    private UUID id;
    private String chatName;
    private Boolean isGroup;
    private LocalDateTime createdAt;
    private UUID createdById;
    private List<UUID> memberIds;
}
