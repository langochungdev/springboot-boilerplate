package com.instar.feature.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private String id;
    private String userId;
    private String type;
    private String message;
    private String link;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
