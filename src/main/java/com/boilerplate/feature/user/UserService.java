package com.boilerplate.feature.user;
import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto findById(UUID id);
    UserDto create(UserDto userDto);
    UserDto update(UUID id, UserDto userDto);
    void delete(UUID id);
    List<User> findAll();
    UserDto findByUsername(String username);
    void changePassword(UUID id, String oldPassword, String newPassword);
    void verifyAccount(UUID id, String code);
}
