package com.boilerplate.feature.user;
import com.boilerplate.common.exception.BusinessException;
import com.boilerplate.common.exception.errorcode.AuthError;
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


    @Override
//    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public void changePassword(UUID id, String oldPassword, String newPassword) {
        log.info("Request to change password for userId={}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException(AuthError.USER_NOT_FOUND));
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
}
