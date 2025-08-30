package com.boilerplate.feature.device.service.impl;
import com.boilerplate.feature.auth.dto.AuthRequest;
import com.boilerplate.feature.device.entity.Device;
import com.boilerplate.feature.device.repository.DeviceRepository;
import com.boilerplate.feature.device.service.DeviceService;
import com.boilerplate.feature.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService{
    private final DeviceRepository deviceRepository;

    public void handleLoginDevice(User user, AuthRequest request) {
        Device device = deviceRepository
                .findByUserIdAndDeviceId(user.getId(), request.getDeviceId())
                .orElse(Device.builder()
                        .user(user)
                        .deviceId(request.getDeviceId())
                        .build());

        device.setDeviceName(request.getDeviceName());
        device.setPushToken(request.getPushToken());
        device.setLastLogin(LocalDateTime.now());
        device.setIsActive(true);
        deviceRepository.save(device);
    }

    public void deactivateDevice(UUID userId, String deviceId) {
        deviceRepository.findByUserIdAndDeviceId(userId, deviceId)
                .ifPresent(d -> {
                    d.setIsActive(false);
                    deviceRepository.save(d);
                });
    }
}

