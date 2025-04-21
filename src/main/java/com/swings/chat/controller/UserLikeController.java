package com.swings.chat.controller;

import com.swings.chat.dto.SentLikeDTO;
import com.swings.chat.service.UserLikeService;
import com.swings.user.service.UserPointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class UserLikeController {

    private final UserLikeService userLikeService;
    private final UserPointService userPointService; // 💰 포인트 차감 서비스

    //  좋아요 요청 (무료 3회 + 이후 유료)
    @PostMapping("/{fromUserId}/{toUserId}")
    public ResponseEntity<String> sendLike(
            @PathVariable String fromUserId,
            @PathVariable String toUserId,
            @RequestParam(required = false, defaultValue = "false") boolean paid
    ) {
        boolean canSendFreeLike = userLikeService.canSendLike(fromUserId);

        if (!canSendFreeLike && !paid) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("무료 좋아요 횟수 초과");
        }

        if (!canSendFreeLike && paid) {
            try {
                userPointService.usePoint(fromUserId, 1, "좋아요 사용");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("포인트 부족");
            }
        }

        userLikeService.likeUser(fromUserId, toUserId);
        return ResponseEntity.ok("좋아요 성공");
    }


    //  매칭 여부 확인
    @GetMapping("/match/{fromUserId}/{toUserId}")
    public ResponseEntity<Boolean> checkMatch(@PathVariable String fromUserId, @PathVariable String toUserId) {
        boolean isMatched = userLikeService.isMatched(fromUserId, toUserId);
        return ResponseEntity.ok(isMatched);
    }

    //  보낸 좋아요
    @GetMapping("/sent")
    public ResponseEntity<List<SentLikeDTO>> getMySentLikes() {
        String currentUsername = "user001"; // FIXME: 로그인 구현 시 수정
        List<SentLikeDTO> result = userLikeService.getSentLikesWithMutual(currentUsername);
        return ResponseEntity.ok(result);
    }

    //  받은 + 보낸 좋아요 통합 리스트
    @GetMapping("/all/{userId}")
    public ResponseEntity<Map<String, List<SentLikeDTO>>> getAllLikes(@PathVariable String userId) {
        return ResponseEntity.ok(userLikeService.getSentAndReceivedLikes(userId));
    }
    //  남은 좋아요 수 조회 API
    @GetMapping("/count/{username}")
    public ResponseEntity<Integer> getDailyLikeCount(@PathVariable String username) {
        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT);
        int count = userLikeService.countTodayLikes(username, todayStart);
        int remaining = Math.max(0, 3 - count); // 하루 3개가 기본
        return ResponseEntity.ok(remaining);
    }


}
