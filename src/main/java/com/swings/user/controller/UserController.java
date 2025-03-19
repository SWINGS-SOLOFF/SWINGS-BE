package com.swings.user.controller;

import com.swings.user.dto.UserDTO;
import com.swings.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 무작위 추천 유저 조회 API
    @GetMapping("/{username}/recommend")
    public ResponseEntity<UserDTO> getRandomUser(@PathVariable String username) {
        UserDTO recommendedUser = userService.getRandomUser(username);
        return ResponseEntity.ok(recommendedUser);
    }

    // 싫어요 후 새로운 유저 추천 API
    @GetMapping("/{username}/next")
    public ResponseEntity<UserDTO> getNextUser(@PathVariable String username, @RequestParam String excludedUsername) {
        UserDTO recommendedUser = userService.getNextRandomUser(username, excludedUsername);
        return ResponseEntity.ok(recommendedUser);
    }

}
