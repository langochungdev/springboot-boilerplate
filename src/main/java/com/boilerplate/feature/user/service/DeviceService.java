package com.boilerplate.feature.user.service;
import com.boilerplate.feature.user.entity.Device;

import java.util.List;
import java.util.UUID;

public interface DeviceService {
    void handleLoginDevice(UUID userId, String deviceId, String deviceName, String pushToken);
    void revokeDevice(UUID userId, UUID deviceId);
    List<Device> getDevicesByUserId(UUID userId);
}
