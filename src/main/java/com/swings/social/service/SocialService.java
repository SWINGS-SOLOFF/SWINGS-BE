package com.swings.social.service;

import com.swings.feed.dto.FeedDTO;
import com.swings.social.dto.SocialDTO;
import com.swings.user.dto.UserDTO;

import java.util.List;

public interface SocialService {
    boolean followUser(Long followerId, Long followeeId);
    boolean unfollowUser(Long followerId, Long followeeId);
    List<UserDTO> getFollowers(Long userId);
    List<UserDTO> getFollowing(Long userId);
    boolean isFollowing(Long followerId, Long followeeId);
    boolean updateIntroduce(Long userId, String introduce);
    String getIntroduce(Long userId);
    int getUserFeedCount(Long userId);
    List<FeedDTO> getFeedsByUserId(Long userId);
}
