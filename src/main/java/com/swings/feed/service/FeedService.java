package com.swings.feed.service;

import com.swings.feed.dto.FeedDTO;
import com.swings.user.dto.UserDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FeedService {
    FeedDTO createFeed(FeedDTO feedDTO);
    List<FeedDTO> getAllFeeds(Pageable pageable);
    Optional<FeedDTO> getFeedById(Long feedId);
    FeedDTO updateFeed(Long feedId, FeedDTO updatedFeedDTO);
    void deleteFeed(Long feedId);
    FeedDTO likeFeed(Long feedId, Long userId);
    FeedDTO unlikeFeed(Long feedId, Long userId);
    int getUserFeedCount(Long userId);
    List<FeedDTO> getFeedsByUserId(Long userId);
    List<UserDTO> getLikedUsers(Long feedId);
    List<FeedDTO> getFeedsRandomized(Long userId, Pageable pageable);
}
