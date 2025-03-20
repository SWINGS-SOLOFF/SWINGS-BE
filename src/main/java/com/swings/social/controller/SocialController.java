package com.swings.social.controller;

import com.swings.feed.service.FeedService;
import com.swings.social.dto.SocialDTO;
import com.swings.social.service.SocialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;
    private final FeedService feedService;

    // 팔로우
    @PostMapping("/follow")
    public ResponseEntity<String> followUser(@RequestBody SocialDTO socialDTO) {
        boolean result = socialService.followUser(socialDTO.getFollowerId(), socialDTO.getFolloweeId());
        if (result) {
            return ResponseEntity.ok("팔로우 성공!");
        } else {
            return ResponseEntity.badRequest().body("팔로우 실패. 이미 팔로우 중이거나 잘못된 요청입니다.");
        }
    }

    // 언팔로우
    @PostMapping("/unfollow")
    public ResponseEntity<String> unfollowUser(@RequestBody SocialDTO socialDTO) {
        boolean result = socialService.unfollowUser(socialDTO.getFollowerId(), socialDTO.getFolloweeId());
        if (result) {
            return ResponseEntity.ok("언팔로우 성공!");
        } else {
            return ResponseEntity.badRequest().body("언팔로우 실패. 팔로우 하지 않은 사용자입니다.");
        }
    }

    // 특정 유저의 팔로워 목록 조회 (해당 유저를 팔로우하는 사용자들)
    @GetMapping("/followers/{userId}")
    public ResponseEntity<?> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFollowers(userId));
    }

    // 특정 유저의 팔로잉 목록 조회 (해당 유저가 팔로우하는 사용자들)
    @GetMapping("/followings/{userId}")
    public ResponseEntity<?> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFollowings(userId));
    }

    // 특정 사용자 간의 팔로우 여부 확인
    @GetMapping("/isFollowing")
    public ResponseEntity<String> isFollowing(
            @RequestParam Long followerId,
            @RequestParam Long followeeId) {
        boolean result = socialService.isFollowing(followerId, followeeId);
        if (result) {
            return ResponseEntity.ok("팔로우 중입니다.");
        } else {
            return ResponseEntity.ok("팔로우하지 않았습니다.");
        }
    }

    // 자기소개 추가 또는 수정
    @PostMapping("/update-bio")
    public ResponseEntity<String> updateBio(@RequestParam Long userId, @RequestBody String bio) {
        boolean result = socialService.updateBio(userId, bio);
        if (result) {
            return ResponseEntity.ok("자기소개가 업데이트되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("자기소개 업데이트에 실패했습니다.");
        }
    }

    // 특정 유저의 자기소개 조회
    @GetMapping("/bio/{userId}")
    public ResponseEntity<String> getBio(@PathVariable Long userId) {
        String bio = socialService.getBio(userId);
        return ResponseEntity.ok(bio);
    }

    @GetMapping("/feeds/count/{userId}")
    public ResponseEntity<Integer> getUserFeedCount(@PathVariable Long userId) {
        int feedCount = feedService.getUserFeedCount(userId);
        return ResponseEntity.ok(feedCount);
    }

}
