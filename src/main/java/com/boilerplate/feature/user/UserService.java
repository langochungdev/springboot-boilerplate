package com.boilerplate.feature.user;
import java.util.UUID;

public interface UserService {
    void changePassword(UUID id, String oldPassword, String newPassword);
    void verifyAccount(UUID id, String code);
}
