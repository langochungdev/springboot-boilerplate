package com.boilerplate.feature.notification;

import com.boilerplate.common.exception.NoPermissionException;
import com.boilerplate.common.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
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
    public List<NotificationDto> findByUserId(UUID userId) {
        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        if (!userId.equals(currentUserId) && !admin) {
            throw new NoPermissionException();
        }
        return notificationRepository.findAll().stream()
                .filter(n -> n.getUser().getId().equals(userId))
                .map(notificationMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void markRead(UUID notificationId) {
        Notification n = notificationRepository.findById(notificationId).orElse(null);
        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        if (n == null || (!n.getUser().getId().equals(currentUserId) && !admin)) {
            throw new NoPermissionException();
        }
        n.setIsRead(true);
        notificationRepository.save(n);
    }
}
