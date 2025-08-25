package com.boilerplate.feature.user;
import com.boilerplate.common.exception.BusinessException;
import com.boilerplate.common.exception.errorcode.UserError;
import com.boilerplate.feature.user.dto.ChangePasswordRequestDto;
import com.boilerplate.feature.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND, id.toString()));
        return userMapper.toDto(user);
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @PutMapping("/{id}")
    public UserDto save(@RequestBody User e) {
        User user =  userRepository.save(e);
        return  userMapper.toDto(user);
    }


    @PutMapping("/{id}/password")
    public String changePassword(@PathVariable UUID id, @RequestBody ChangePasswordRequestDto req) {
        userService.changePassword(id, req.getOldPassword(), req.getNewPassword());
        return "Đổi mật khẩu thành công";
    }


    @PostMapping("/{id}/verify")
    public String verify(@PathVariable UUID id, @RequestParam String code) {
        userService.verifyAccount(id, code);
        return "Kích hoạt tài khoản thành công";
    }
}
