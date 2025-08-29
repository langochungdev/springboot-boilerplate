package com.boilerplate.feature.notification.mapper;

import com.boilerplate.feature.notification.dto.NotificationDto;
import com.boilerplate.feature.notification.entity.Notification;
import com.boilerplate.feature.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {
    public NotificationDto toDto(Notification e) {
        return NotificationDto.builder()
                .id(e.getId())
                .userId(e.getUser().getId())
                .type(e.getType())
                .message(e.getMessage())
                .link(e.getLink())
                .isRead(e.getIsRead())
                .createdAt(e.getCreatedAt())
                .build();
    }

    public Notification toEntity(NotificationDto dto, User user) {
        return Notification.builder()
                .id(dto.getId())
                .user(user)
                .type(dto.getType())
                .message(dto.getMessage())
                .link(dto.getLink())
                .isRead(dto.getIsRead())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
