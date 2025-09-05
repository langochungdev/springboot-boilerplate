package com.boilerplate.feature.user.controller;
import com.boilerplate.common.util.CurrentUserUtil;
import com.boilerplate.feature.user.entity.Device;
import com.boilerplate.feature.user.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/me/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;
    private final CurrentUserUtil currentUserUtil;

    @GetMapping
    public ResponseEntity<List<Device>> getMyDevices() {
        UUID userId = currentUserUtil.getUser().getId();
        return ResponseEntity.ok(deviceService.getDevicesByUserId(userId));
    }

    @DeleteMapping("/{deviceId}")
    public ResponseEntity<Void> revokeDevice(@PathVariable UUID deviceId) {
        UUID userId = currentUserUtil.getUser().getId();
        deviceService.revokeDevice(userId, deviceId);
        return ResponseEntity.noContent().build();
    }
}
