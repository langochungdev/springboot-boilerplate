package com.instar.feature.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatUserDto {
    private String chatId;
    private String userId;
    private Boolean isAdmin;
}
