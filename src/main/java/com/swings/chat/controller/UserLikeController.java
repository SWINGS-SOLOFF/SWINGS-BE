package com.swings.chat.controller;


import com.swings.chat.service.UserLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class UserLikeController {

    private final UserLikeService userLikeService;

    // 좋아요 추가 API
    @PostMapping("/{fromUserId}/{toUserId}")
    public ResponseEntity<String> likeUser(@PathVariable String fromUserId, @PathVariable String toUserId) {
        userLikeService.likeUser(fromUserId, toUserId);
        return ResponseEntity.ok("좋아요를 눌렀습니다.");
    }

    // 매칭 여부 확인 API
    @GetMapping("/match/{fromUserId}/{toUserId}")
    public ResponseEntity<Boolean> checkMatch(@PathVariable String fromUserId, @PathVariable String toUserId) {
        boolean isMatched = userLikeService.isMatched(fromUserId, toUserId);
        return ResponseEntity.ok(isMatched);
    }
}