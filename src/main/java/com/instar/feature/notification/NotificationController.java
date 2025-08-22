package com.instar.feature.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public List<NotificationDto> getNotifications(@PathVariable UUID userId) {
        return notificationService.findByUserId(userId);
    }

    @PutMapping("/{id}/read")
    public String markRead(@PathVariable UUID id) {
        notificationService.markRead(id);
        return "Đã đọc thông báo";
    }
}
