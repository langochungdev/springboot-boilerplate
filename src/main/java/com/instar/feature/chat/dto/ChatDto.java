package com.instar.feature.chat.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatDto {
    private String id;
    private String chatName;
    private Boolean isGroup;
    private String createdById;
    private LocalDateTime createdAt;
    private List<String> memberIds; // danh sách user id trong nhóm
}
