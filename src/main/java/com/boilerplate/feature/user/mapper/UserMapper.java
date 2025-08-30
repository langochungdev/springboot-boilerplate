package com.boilerplate.feature.user.mapper;

import com.boilerplate.feature.user.dto.UserDto;
import com.boilerplate.feature.user.entity.Role;
import com.boilerplate.feature.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    public UserDto toDto(User e) {
        return UserDto.builder()
                .id(e.getId())
                .username(e.getUsername())
                .email(e.getEmail())
                .fullName(e.getFullName())
                .avatarUrl(e.getAvatarUrl())
                .bio(e.getBio())
                .createdAt(e.getCreatedAt())
                .lastLogin(e.getLastLogin())
                .isActive(e.getIsActive())
                .isVerified(e.getIsVerified())
                .roles(
                        e.getRoles() != null
                                ? e.getRoles().stream().map(Role::getName).collect(Collectors.toSet())
                                : Set.of()
                )
                .build();
    }

    public User toEntity(UserDto dto, Set<Role> roles) {
        return User.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .fullName(dto.getFullName())
                .avatarUrl(dto.getAvatarUrl())
                .bio(dto.getBio())
                .createdAt(dto.getCreatedAt())
                .lastLogin(dto.getLastLogin())
                .isActive(dto.getIsActive())
                .isVerified(dto.getIsVerified())
                .roles(roles)
                .build();
    }
}
