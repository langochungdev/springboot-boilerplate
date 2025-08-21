package com.instar.feature.device;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, String> {
    Optional<Device> findByUserIdAndFingerprint(String userId, String fingerprint);
    List<Device> findAllByUserId(String userId);
}
