package com.boilerplate.feature.user.service.impl;
import com.boilerplate.common.exception.BusinessException;
import com.boilerplate.common.exception.errorcode.UserError;
import com.boilerplate.feature.user.dto.UserDto;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.mapper.UserMapper;
import com.boilerplate.feature.user.repository.UserRepository;
import com.boilerplate.feature.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @CachePut(value = "users", key = "#userId")
    public UserDto updateProfile(UUID userId, UserDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        userMapper.updateEntityFromDto(dto, user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    @Override
    @Cacheable(value = "users", key = "{#page, #size, #search}")
    public Page<UserDto> getAllUsers(int page, int size, String search) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users;
        if (search != null && !search.isBlank()) {
            users = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }
        return users.map(userMapper::toDto);
    }

    @Override
    @Cacheable(value = "users", key = "#userId")
    public UserDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        return userMapper.toDto(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void lockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public void unlockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        user.setIsActive(true);
        userRepository.save(user);
    }
}
