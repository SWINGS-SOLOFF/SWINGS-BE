package com.swings.notification.controller;

import com.swings.user.entity.UserEntity;
import com.swings.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fcm")
@RequiredArgsConstructor
public class FCMController {

    private final UserService userService;

    @PostMapping("/register-token")
    public ResponseEntity<String> registerFcmToken(@RequestParam String username, @RequestBody String token) {
        System.out.println("[FCMController] 토큰 등록 요청: " + username + " → " + token);

        UserEntity currentUser = userService.getUserByUsername(username);
        userService.updatePushToken(currentUser.getUsername(), token);

        return ResponseEntity.ok("✅ FCM 토큰 저장 완료");
    }
}