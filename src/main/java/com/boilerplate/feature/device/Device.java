package com.boilerplate.feature.device;
import com.boilerplate.feature.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, columnDefinition = "uniqueidentifier")
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(length = 255)
    private String deviceToken;

    @Column( length = 100)
    private String deviceName;

    @Column(length = 255)
    private String fingerprint;

    @Column(nullable = false)
    private LocalDateTime lastLogin;

    @Column(nullable = false)
    private Boolean isActive = true;


}
