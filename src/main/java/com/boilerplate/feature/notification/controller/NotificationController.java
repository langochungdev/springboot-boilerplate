package com.boilerplate.feature.notification.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    @GetMapping
    public String test() {
        return "Only admin, user can access /api/notifications";
    }
}
