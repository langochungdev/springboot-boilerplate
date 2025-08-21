package com.instar.feature.user;
import java.util.List;

public interface UserService {
    UserDto findById(String id);
    UserDto create(UserDto userDto);
    UserDto update(String id, UserDto userDto);
    void delete(String id);
    List<User> findAll();
    UserDto findByUsername(String username);
    void changePassword(String id, String oldPassword, String newPassword);
    void verifyAccount(String id, String code);
}
