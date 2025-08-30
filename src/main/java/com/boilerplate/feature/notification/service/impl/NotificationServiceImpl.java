package com.boilerplate.feature.notification.service.impl;

import com.boilerplate.feature.notification.dto.NotificationDto;
import com.boilerplate.feature.notification.entity.Notification;
import com.boilerplate.feature.notification.mapper.NotificationMapper;
import com.boilerplate.feature.notification.repository.NotificationRepository;
import com.boilerplate.feature.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public List<NotificationDto> findByUserId(UUID userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    public void markRead(UUID notificationId) {
        Notification n = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        n.setIsRead(true);
        notificationRepository.save(n);
    }
}
