package com.boilerplate.feature.notification.service.impl;
import com.boilerplate.feature.notification.dto.NotificationDto;
import com.boilerplate.feature.notification.entity.Notification;
import com.boilerplate.feature.notification.mapper.NotificationMapper;
import com.boilerplate.feature.notification.repository.NotificationRepository;
import com.boilerplate.feature.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public List<NotificationDto> findByUserId(UUID userId) {
        return notificationRepository.findAll().stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public void markRead(UUID notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElse(null);
        n.setIsRead(true);
        notificationRepository.save(n);
    }
}
