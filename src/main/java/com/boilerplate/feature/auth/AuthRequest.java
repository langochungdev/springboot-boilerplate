package com.boilerplate.feature.auth;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    private String deviceToken;
    private String deviceName;
    private String fingerprint;
}
