package com.swings.user.controller;

import com.swings.user.entity.UserEntity;
import com.swings.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    // 전체 유저 조회
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // 유저 상세 조회
    @GetMapping("/users/{username}")
    public ResponseEntity<UserEntity> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // 유저 삭제 (강제 탈퇴)
    @DeleteMapping("/users/{username}/delete")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok("유저 삭제 완료");
    }

    // 유저 역할 변경
    @PatchMapping("/users/{username}/role")
    public ResponseEntity<String> changeRole(@PathVariable String username, @RequestParam String role) {
        userService.updateUserRole(username, role);
        return ResponseEntity.ok("역할 변경 완료");
    }
}