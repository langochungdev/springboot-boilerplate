package com.instar.feature.user;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable String id, @RequestBody UserDto dto) {
        return userService.update(id, dto);
    }


    @PutMapping("/{id}/password")
    public String changePassword(@PathVariable String id, @RequestBody ChangePasswordRequestDto req) {
        userService.changePassword(id, req.getOldPassword(), req.getNewPassword());
        return "Đổi mật khẩu thành công";
    }


    @PostMapping("/{id}/verify")
    public String verify(@PathVariable String id, @RequestParam String code) {
        userService.verifyAccount(id, code);
        return "Kích hoạt tài khoản thành công";
    }
}
