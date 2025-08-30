package com.boilerplate.feature.chat.dto;

import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatUserDto {
    private UUID chatId;
    private UUID userId;
    private boolean isAdmin;
}
