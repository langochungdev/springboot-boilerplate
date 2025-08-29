package com.boilerplate.feature.user.service.impl;

import com.boilerplate.common.util.CurrentUserUtil;
import com.boilerplate.feature.auth.dto.AuthResponse;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.repository.UserRepository;
import com.boilerplate.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserUtil currentUserUtil;

    @Override
//    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public void changePassword(UUID id, String oldPassword, String newPassword) {
        log.info("Request to change password for userId={}", id);
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            log.warn("User with id={} not found, cannot change password", id);
            return;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for userId={}", id);
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public void verifyAccount(UUID id, String code) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    public AuthResponse.User checkStatus() {
        User user = currentUserUtil.getUser();
        return AuthResponse.User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
