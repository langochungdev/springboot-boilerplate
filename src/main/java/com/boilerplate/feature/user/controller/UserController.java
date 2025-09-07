package com.boilerplate.feature.user.controller;
import com.boilerplate.common.util.CurrentUserUtil;
import com.boilerplate.feature.user.dto.UserDto;
import com.boilerplate.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CurrentUserUtil currentUserUtil;

    @GetMapping
    public ResponseEntity<UserDto> checkStatus() {
        UUID userId = currentUserUtil.getUser().getId();
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateProfile(@RequestBody UserDto request) {
        UUID userId = currentUserUtil.getUser().getId();
        return ResponseEntity.ok(userService.updateProfile(userId, request));
    }
}
