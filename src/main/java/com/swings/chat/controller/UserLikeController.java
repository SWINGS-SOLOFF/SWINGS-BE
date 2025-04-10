package com.swings.chat.controller;


import com.swings.chat.dto.SentLikeDTO;
import com.swings.chat.entity.UserLikeEntity;
import com.swings.chat.service.UserLikeService;
import com.swings.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class UserLikeController {

    private final UserLikeService userLikeService;
    private final UserService userService;

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

    @GetMapping("/sent")
    public ResponseEntity<List<SentLikeDTO>> getMySentLikes() {
        String currentUsername = userService.getCurrentUser().getUsername();
        System.out.println("✅ currentUsername: " + currentUsername); // 로그 찍기
        List<SentLikeDTO> result = userLikeService.getSentLikesWithMutual(currentUsername);
        return ResponseEntity.ok(result);
    }

    // ✅ 통합 API: 보낸 + 받은 좋아요
    @GetMapping("/all/{userId}")
    public ResponseEntity<Map<String, List<SentLikeDTO>>> getAllLikes(@PathVariable String userId) {
        return ResponseEntity.ok(userLikeService.getSentAndReceivedLikes(userId));
    }


}