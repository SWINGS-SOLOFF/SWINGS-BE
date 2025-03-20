package com.swings.user.controller;

import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import com.swings.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

//@CrossOrigin(origins = "http://localhost:5173") // 프론트엔드 요청 허용
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO dto) {
        UserEntity newUser = userService.registerUser(dto);
        return ResponseEntity.ok("회원가입 성공! ID: " + newUser.getUserId());
    }

    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userService.isUsernameExists(username));
        return ResponseEntity.ok(response);
    }

    //회원정보 수정
    @PatchMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable Long userId, @RequestBody UserDTO dto) {
        UserEntity updatedUser = userService.updateUser(userId, dto);
        return ResponseEntity.ok("회원 정보 수정 완료! ID:" + updatedUser.getUserId());
    }
}
