package com.swings.social.repository;

import com.swings.feed.entity.FeedEntity;
import com.swings.social.entity.SocialEntity;
import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialRepository extends JpaRepository<SocialEntity, Long> {

    boolean existsByFollowerAndFollowee(UserEntity follower, UserEntity followee);

    Optional<SocialEntity> findByFollowerAndFollowee(UserEntity follower, UserEntity followee);

    // 특정 유저가 팔로워인 SocialEntity 리스트
    List<SocialEntity> findByFollowee(UserEntity followee);

    // 특정 유저가 팔로잉인 SocialEntity 리스트
    List<SocialEntity> findByFollower(UserEntity follower);

    // 특정 유저가 팔로잉한 모든 피드를 가져오는 메서드
    List<FeedEntity> findByFollower_UserId(Long userId);

    // 특정 유저가 팔로우한 모든 피드를 가져오는 메서드
    List<FeedEntity> findByFollowee_UserId(Long userId);

}
