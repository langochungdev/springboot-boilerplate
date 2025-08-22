package com.instar.feature.device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeviceRepository extends JpaRepository<Device, UUID> {
    Optional<Device> findByUserIdAndFingerprint(UUID userId, String fingerprint);
    List<Device> findAllByUserId(UUID userId);
}
