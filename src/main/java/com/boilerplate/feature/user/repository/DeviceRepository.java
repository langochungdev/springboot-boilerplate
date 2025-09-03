package com.boilerplate.feature.user.repository;

import com.boilerplate.feature.user.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByUserIdAndDeviceId(UUID userId, String deviceId);
    List<Device> findByUserId(UUID userId);
}
