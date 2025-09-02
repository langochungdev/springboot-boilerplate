package com.boilerplate.feature.chat.mapper;
import com.boilerplate.feature.chat.dto.ChatDto;
import com.boilerplate.feature.chat.entity.Chat;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ChatMapper {
    public ChatDto toDto(Chat chat) {
        List<UUID> memberIds = chat.getChatUsers() != null
                ? chat.getChatUsers().stream()
                .map(cu -> cu.getUser().getId())
                .collect(Collectors.toList())
                : List.of();

        return ChatDto.builder()
                .id(chat.getId())
                .chatName(chat.getChatName())
                .isGroup(chat.isGroup())
                .createdAt(chat.getCreatedAt())
                .createdById(chat.getCreatedBy() != null ? chat.getCreatedBy().getId() : null)
                .memberIds(memberIds)
                .build();
    }
}
