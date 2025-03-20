package com.swings.social.repository;

import com.swings.social.entity.SocialEntity;
import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SocialRepository extends JpaRepository<SocialEntity, Long> {

    boolean existsByFollowerAndFollowee(UserEntity follower, UserEntity followee);

    Optional<SocialEntity> findByFollowerAndFollowee(UserEntity follower, UserEntity followee);

    // 특정 유저가 팔로워
    List<SocialEntity> findByFollowee(UserEntity followee);

    // 특정 유저가 팔로잉
    List<SocialEntity> findByFollower(UserEntity follower);
}
