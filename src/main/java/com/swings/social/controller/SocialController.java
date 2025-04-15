package com.swings.social.controller;

import com.swings.feed.dto.FeedDTO;
import com.swings.feed.service.FeedService;
import com.swings.social.dto.SocialDTO;
import com.swings.social.service.SocialService;
import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/social")
@RequiredArgsConstructor
public class SocialController {

    private final SocialService socialService;
    private final FeedService feedService;
    private final UserRepository userRepository;


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
    public ResponseEntity<List<UserDTO>> getFollowers(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFollowers(userId));
    }
    
    // 특정 유저의 팔로잉 목록 조회 (해당 유저가 팔로우하는 사용자들)
    @GetMapping("/followings/{userId}")
    public ResponseEntity<List<UserDTO>> getFollowings(@PathVariable Long userId) {
        return ResponseEntity.ok(socialService.getFollowing(userId));
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
    @PostMapping("/update-introduce")
    public ResponseEntity<String> updateIntroduce(@RequestParam Long userId, @RequestBody String introduce) {
        boolean result = socialService.updateIntroduce(userId, introduce);
        if (result) {
            return ResponseEntity.ok("자기소개가 업데이트되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("자기소개 업데이트에 실패했습니다.");
        }
    }

    // 특정 유저의 자기소개 조회
    @GetMapping("/introduce/{userId}")
    public ResponseEntity<String> getIntroduce(@PathVariable Long userId) {
        String introduce = socialService.getIntroduce(userId);
        return ResponseEntity.ok(introduce);
    }
    
    // 유저 피드 갯수 조회
    @GetMapping("/feeds/count/{userId}")
    public ResponseEntity<Integer> getUserFeedCount(@PathVariable Long userId) {
        int feedCount = feedService.getUserFeedCount(userId);
        return ResponseEntity.ok(feedCount);
    }

    // 특정 사용자의 피드 조회 (모든 사용자 가능)
    @GetMapping("/feeds/user/{userId}")
    public ResponseEntity<?> getUserFeeds(@PathVariable Long userId) {
        List<FeedDTO> userFeeds = feedService.getFeedsByUserId(userId);
        return ResponseEntity.ok(userFeeds);
    }

    // 특정 사용자 정보 조회 (ID 기반)
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return ResponseEntity.ok(convertToDTO(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private UserDTO convertToDTO(UserEntity user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .birthDate(user.getBirthDate().toString())
                .phonenumber(user.getPhonenumber())
                .email(user.getEmail())
                .job(user.getJob())
                .golfSkill(user.getGolfSkill().name())
                .mbti(user.getMbti())
                .hobbies(user.getHobbies())
                .religion(user.getReligion())
                .smoking(user.getSmoking().name())
                .drinking(user.getDrinking().name())
                .introduce(user.getIntroduce())
                .userImg(user.getUserImg())
                .role(user.getRole().name())
                .gender(user.getGender().name())
                .activityRegion(user.getActivityRegion().name())
                .build();
    }
    

}
