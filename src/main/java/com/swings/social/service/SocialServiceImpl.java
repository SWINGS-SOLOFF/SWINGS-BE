package com.swings.social.service;

import com.swings.feed.dto.FeedDTO;
import com.swings.feed.entity.FeedEntity;
import com.swings.feed.repository.FeedRepository;
import com.swings.social.dto.SocialDTO;
import com.swings.social.entity.SocialEntity;
import com.swings.social.repository.SocialRepository;
import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final FeedRepository feedRepository;

    @Override
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

    @Override
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

    @Override
    public List<UserDTO> getFollowers(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return socialRepository.findByFollowee(user)
            .stream()
            .map(social -> convertToUserDTO(social.getFollower()))
            .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getFollowing(Long userId) {
        UserEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        return socialRepository.findByFollower(user)
            .stream()
            .map(social -> convertToUserDTO(social.getFollowee()))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isFollowing(Long followerId, Long followeeId) {
        Optional<UserEntity> follower = userRepository.findById(followerId);
        Optional<UserEntity> followee = userRepository.findById(followeeId);
        if (follower.isPresent() && followee.isPresent()) {
            return socialRepository.existsByFollowerAndFollowee(follower.get(), followee.get());
        }
        return false;
    }

    @Override
    public boolean updateIntroduce(Long userId, String introduce) {
        return userRepository.findById(userId).map(user -> {
            if (introduce != null && !introduce.trim().isEmpty()) {
                user.setIntroduce(introduce);
                userRepository.save(user);
                return true;
            }
            return false;
        }).orElse(false);
    }

    @Override
    public String getIntroduce(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getIntroduce() != null ? user.getIntroduce() : "자기소개가 없습니다.")
                .orElse("자기소개가 없습니다.");
    }

    @Override
    public int getUserFeedCount(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        return feedRepository.countByUser_UserId(userId);
    }

    @Override
    public List<FeedDTO> getFeedsByUserId(Long userId) {
        List<FeedEntity> feeds = feedRepository.findByUser_UserId(userId);
        return feeds.stream()
                .map(FeedDTO::new)
                .collect(Collectors.toList());
    }

    // UserEntity → UserDTO 변환
    private UserDTO convertToUserDTO(UserEntity user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .userImg(
                		  user.getUserImg() != null && !user.getUserImg().trim().equals("") ? 
                		  user.getUserImg() : 
                		  null
                		)                .introduce(user.getIntroduce())
                .email(user.getEmail())
                .gender(user.getGender() != null ? user.getGender().toString() : null)
                .activityRegion(user.getActivityRegion() != null ? user.getActivityRegion().toString() : null)
                .build();
    }
}
