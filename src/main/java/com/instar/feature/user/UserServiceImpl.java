package com.instar.feature.user;
import com.instar.common.exception.NoPermissionException;
import com.instar.common.util.CurrentUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto findById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElse(null);
    }

    @Override
    public UserDto create(UserDto dto) {
        boolean admin = CurrentUserUtil.isAdmin();
        if (!admin) {
            throw new NoPermissionException();
        }
        User e = userMapper.toEntity(dto);
        e = userRepository.save(e);
        return userMapper.toDto(e);
    }

    @Override
    public UserDto update(UUID id, UserDto dto) {
        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        if (!id.equals(currentUserId) && !admin) {
            throw new NoPermissionException();
        }
        User e = userRepository.findById(id).orElse(null);
        if (e == null) return null;
        e.setFullName(dto.getFullName());
        e.setAvatarUrl(dto.getAvatarUrl());
        e.setBio(dto.getBio());
        e.setIsActive(dto.getIsActive());
        e.setIsVerified(dto.getIsVerified());
        e.setLastLogin(dto.getLastLogin());
        e.setRole(dto.getRole());
        e = userRepository.save(e);
        return userMapper.toDto(e);
    }


    @Override
    public void delete(UUID id) {
        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        if (!id.equals(currentUserId) && !admin) {
            throw new NoPermissionException();
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> findAll() {
        boolean admin = CurrentUserUtil.isAdmin();
        if (!admin) {
            throw new NoPermissionException();
        }
        return userRepository.findAll();
    }

    @Override
    public UserDto findByUsername(String username) {
        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        User user = userRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        if (user == null) return null;
        if (!user.getId().equals(currentUserId) && !admin) {
            throw new NoPermissionException();
        }
        return userMapper.toDto(user);
    }

    @Override
    public void changePassword(UUID id, String oldPassword, String newPassword) {
        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        if (!id.equals(currentUserId) && !admin) {
            throw new NoPermissionException();
        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        if (!admin && !passwordEncoder.matches(oldPassword, user.getPassword())) return;
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void verifyAccount(UUID id, String code) {
        UUID currentUserId = CurrentUserUtil.getCurrentUserId();
        boolean admin = CurrentUserUtil.isAdmin();
        if (!id.equals(currentUserId) && !admin) {
            throw new NoPermissionException();
        }
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        user.setIsVerified(true);
        userRepository.save(user);
    }
}
