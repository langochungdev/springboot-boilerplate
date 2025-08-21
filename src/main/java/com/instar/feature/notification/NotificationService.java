package com.instar.feature.notification;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> findByUserId(String userId);
    void markRead(String notificationId);
}
