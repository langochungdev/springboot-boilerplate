package com.boilerplate.feature.notification.service;

import com.boilerplate.feature.notification.dto.NotificationDto;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<NotificationDto> findByUserId(UUID userId);
    void markRead(UUID notificationId);
}
