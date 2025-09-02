package com.boilerplate.feature.user.service.impl;
import com.boilerplate.common.exception.BusinessException;
import com.boilerplate.common.util.CurrentUserUtil;
import com.boilerplate.feature.auth.dto.AuthResponse;
import com.boilerplate.feature.user.dto.UserDto;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.mapper.UserMapper;
import com.boilerplate.feature.user.repository.UserRepository;
import com.boilerplate.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserUtil currentUserUtil;
    private final UserMapper userMapper;

    @Override
    public void changePassword(UUID id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null){
            return;
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Password changed successfully for userId={}", id);
    }

    @Override
    public void verifyAccount(UUID id, String code) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        user.setIsVerified(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto checkStatus() {
        UUID userId = currentUserUtil.getCurrentUserId();
        User user = userRepository.findByIdWithRoles(userId)
                .orElseThrow(null);
        return userMapper.toDto(user);
    }
}
