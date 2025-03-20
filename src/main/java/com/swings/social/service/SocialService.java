package com.swings.social.service;

import com.swings.feed.repository.FeedRepository;
import com.swings.social.entity.SocialEntity;
import com.swings.social.repository.SocialRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final FeedRepository feedRepository;


    // 기존 팔로우 기능
    public boolean followUser(Long followerId, Long followeeId) {
        Optional<UserEntity> follower = userRepository.findById(followerId);
        Optional<UserEntity> followee = userRepository.findById(followeeId);

        if (follower.isPresent() && followee.isPresent()) {
            if (socialRepository.existsByFollowerAndFollowee(follower.get(), followee.get())) {
                return false;
            }
            SocialEntity socialEntity = new SocialEntity();
            socialEntity.setFollower(follower.get());
            socialEntity.setFollowee(followee.get());
            socialRepository.save(socialEntity);
            return true;
        }
        return false;
    }

    // 기존 언팔로우 기능
    public boolean unfollowUser(Long followerId, Long followeeId) {
        Optional<UserEntity> follower = userRepository.findById(followerId);
        Optional<UserEntity> followee = userRepository.findById(followeeId);

        if (follower.isPresent() && followee.isPresent()) {
            Optional<SocialEntity> socialEntity = socialRepository.findByFollowerAndFollowee(follower.get(), followee.get());
            if (socialEntity.isEmpty()) {
                return false;
            }
            socialRepository.delete(socialEntity.get());
            return true;
        }
        return false;
    }

    // 특정 유저의 팔로워 목록 조회 (해당 유저를 팔로우하는 사용자들)
    public List<UserEntity> getFollowers(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        // followee가 user인 모든 SocialEntity에서 follower 리스트 반환
        return socialRepository.findByFollowee(user)
                .stream()
                .map(SocialEntity::getFollower)
                .collect(Collectors.toList());
    }

    // 특정 유저의 팔로잉 목록 조회 (해당 유저가 팔로우하는 사용자들)
    public List<UserEntity> getFollowings(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        // follower가 user인 모든 SocialEntity에서 followee 리스트 반환
        return socialRepository.findByFollower(user)
                .stream()
                .map(SocialEntity::getFollowee)
                .collect(Collectors.toList());
    }

    // 특정 사용자 간의 팔로우 여부 확인
    public boolean isFollowing(Long followerId, Long followeeId) {
        Optional<UserEntity> follower = userRepository.findById(followerId);
        Optional<UserEntity> followee = userRepository.findById(followeeId);
        if (follower.isPresent() && followee.isPresent()) {
            return socialRepository.existsByFollowerAndFollowee(follower.get(), followee.get());
        }
        return false;
    }

    // 자기소개 업데이트
    public boolean updateBio(Long userId, String bio) {
        return userRepository.findById(userId).map(user -> {
            if (bio != null && !bio.trim().isEmpty()) {
                user.setBio(bio);
                userRepository.save(user);
                return true;
            }
            return false;
        }).orElse(false);
    }

    // 특정 유저의 자기소개 조회
    public String getBio(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getBio() != null ? user.getBio() : "자기소개가 없습니다.")
                .orElse("자기소개가 없습니다.");
    }

    // 특정 사용자의 피드 개수를 조회
    public int getUserFeedCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return feedRepository.countByUser_UserId(userId);
    }


}
