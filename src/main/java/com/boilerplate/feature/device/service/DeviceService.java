package com.boilerplate.feature.device.service;
import com.boilerplate.feature.auth.dto.AuthRequest;
import com.boilerplate.feature.user.entity.User;

import java.util.UUID;

public interface DeviceService {
    void handleLoginDevice(User user, AuthRequest request);
    void deactivateDevice(UUID userId, String deviceId);
}
