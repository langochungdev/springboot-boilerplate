package com.boilerplate.feature.auth.dto;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private Long expiresIn;
    private User user;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class User {
        private UUID id;
        private String username;
        private String email;
        private String fullName;
        private Set<String> roles;
    }
}

