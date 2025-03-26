package com.swings.chat.controller;

import com.swings.chat.dto.UserSelectDTO;
import com.swings.chat.service.UserRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRecommendController {
    private final UserRecommendService userRecommendService;

    // 무작위 추천 유저 조회 API
    @GetMapping("/{username}/recommend")
    public ResponseEntity<UserSelectDTO> getRandomUser(@PathVariable String username) {
        UserSelectDTO recommendedUser = userRecommendService.getRandomUser(username);
        return ResponseEntity.ok(recommendedUser);
    }

    // 싫어요 후 새로운 유저 추천 API
    @GetMapping("/{username}/next")
    public ResponseEntity<UserSelectDTO> getNextUser(@PathVariable String username, @RequestParam String excludedUsername) {
        UserSelectDTO recommendedUser = userRecommendService.getNextRandomUser(username, excludedUsername);
        return ResponseEntity.ok(recommendedUser);
    }

}
