package com.boilerplate.feature.user.service.impl;
import com.boilerplate.feature.user.entity.Device;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.repository.DeviceRepository;
import com.boilerplate.feature.user.repository.UserRepository;
import com.boilerplate.feature.user.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService{
    private final DeviceRepository deviceRepository;
    private final UserRepository userRepository;

    public void handleLoginDevice(UUID userId, String deviceId, String deviceName, String pushToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Device device = deviceRepository
                .findByUserIdAndDeviceId(userId, deviceId)
                .orElse(Device.builder()
                        .user(user)
                        .deviceId(deviceId)
                        .build());

        device.setDeviceName(deviceName);
        device.setPushToken(pushToken);
        device.setLastLogin(LocalDateTime.now());
        device.setIsActive(true);

        deviceRepository.save(device);
    }

    @Override
    public void revokeDevice(UUID userId, UUID deviceId) {
        deviceRepository.findById(deviceId)
                .filter(d -> d.getUser().getId().equals(userId))
                .ifPresent(d -> {
                    d.setIsActive(false);
                    deviceRepository.save(d);
                });
    }

    @Override
    public List<Device> getDevicesByUserId(UUID userId) {
        return deviceRepository.findByUserId(userId);
    }
}

