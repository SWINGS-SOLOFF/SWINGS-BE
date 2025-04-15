package com.swings.feed.service;

import com.swings.feed.dto.CommentDTO;
import com.swings.feed.dto.FeedDTO;
import com.swings.feed.entity.FeedEntity;
import com.swings.feed.entity.CommentEntity;
import com.swings.feed.repository.FeedRepository;
import com.swings.notification.service.FCMService;
import com.swings.social.dto.SocialDTO;
import com.swings.social.service.SocialService;
import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final SocialService socialService;
    private final FCMService fcmService;

    public FeedServiceImpl(FeedRepository feedRepository, UserRepository userRepository, SocialService socialService, FCMService fcmService) {
        this.feedRepository = feedRepository;
        this.userRepository = userRepository;
        this.socialService = socialService;
        this.fcmService = fcmService;
    }

    @Override
    public FeedDTO createFeed(FeedDTO feedDTO) {
        FeedEntity feedEntity = convertToEntity(feedDTO);
        FeedEntity savedFeed = feedRepository.save(feedEntity);
        return convertToDTO(savedFeed, feedDTO.getUserId());
    }

    @Override
    public List<FeedDTO> getAllFeeds(Pageable pageable, Long currentUserId) {
        return feedRepository.findAll(pageable).stream()
                .map(feed -> convertToDTO(feed, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FeedDTO> getFeedById(Long feedId) {
        return feedRepository.findById(feedId)
                .map(feed -> convertToDTO(feed, null));
    }

    @Override
    public FeedDTO updateFeed(Long feedId, FeedDTO updatedFeedDTO) {
        return feedRepository.findById(feedId).map(feed -> {
            feed.setCaption(updatedFeedDTO.getCaption());
            feed.setImageUrl(updatedFeedDTO.getImageUrl());
            return convertToDTO(feedRepository.save(feed), updatedFeedDTO.getUserId());
        }).orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
    }

    @Override
    public void deleteFeed(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
        System.out.println("Deleting feed with ID: " + feedId);
        feedRepository.delete(feed);
        System.out.println("Feed deleted successfully with ID: " + feedId);
    }

    @Override
    public FeedDTO likeFeed(Long feedId, Long userId) {
        FeedEntity feed = getFeedEntityOrThrow(feedId);
        UserEntity user = getUserEntityOrThrow(userId);

        boolean alreadyLiked = feed.getLikedUsers().stream()
                .anyMatch(u -> u.getUserId().equals(userId));

        if (!alreadyLiked) {
            feed.getLikedUsers().add(user);
            feed.setLikes(feed.getLikedUsers().size());

            // 피드 작성자에게 푸시 알림 전송
            UserEntity owner = feed.getUser();
            if (owner != null && owner.getPushToken() != null) {
                fcmService.sendPush(
                        owner.getPushToken(),
                        "❤️ 좋아요 알림",
                        user.getUsername() + "님이 '" + feed.getCaption() + "'을 좋아합니다!"
                );
            }
        }
        return convertToDTO(feedRepository.save(feed), userId);
    }

    @Override
    public FeedDTO unlikeFeed(Long feedId, Long userId) {
        FeedEntity feed = getFeedEntityOrThrow(feedId);
        feed.getLikedUsers().removeIf(u -> Objects.equals(u.getUserId(), userId));
        feed.setLikes(feed.getLikedUsers().size());
        return convertToDTO(feedRepository.save(feed), userId);
    }

    @Override
    public int getUserFeedCount(Long userId) {
        if (userId == null) throw new IllegalArgumentException("UserId must not be null");
        return feedRepository.countByUser_UserId(userId);
    }

    @Override
    public List<FeedDTO> getFeedsByUserId(Long userId) {
        return feedRepository.findByUserUserIdOrderByCreatedAtDesc(userId).stream()
                .map(feed -> convertToDTO(feed, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> getLikedUsers(Long feedId) {
        return getFeedEntityOrThrow(feedId).getLikedUsers().stream()
                .map(user -> UserDTO.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .userImg(user.getUserImg() != null ? user.getUserImg() : "default-image-url")
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedDTO> getFeedsRandomized(Long userId, Pageable pageable) {
        List<FeedEntity> feeds = new ArrayList<>(feedRepository.findAll(pageable).getContent());
        feeds.removeIf(feed -> feed.getUser().getUserId().equals(userId));
        Collections.shuffle(feeds);
        return feeds.stream()
                .map(feed -> convertToDTO(feed, userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedDTO> getFeedsByUserList(List<Long> userIds, Pageable pageable) {
        if (userIds == null || userIds.isEmpty()) return Collections.emptyList();
        return feedRepository.findByUser_UserIdIn(userIds, pageable).getContent().stream()
                .map(feed -> convertToDTO(feed, null))
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedDTO> getFeedsByUserListExcludingSelf(List<Long> userIds, Pageable pageable, Long currentUserId) {
        if (userIds == null || userIds.isEmpty()) return Collections.emptyList();
        return feedRepository.findByUser_UserIdIn(userIds, pageable).getContent().stream()
                .filter(feed -> !feed.getUser().getUserId().equals(currentUserId))
                .map(feed -> convertToDTO(feed, currentUserId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getFolloweeIds(Long userId) {
        return socialService.getFollowing(userId).stream()
            .map(UserDTO::getUserId)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<FeedDTO> getAllFeeds(Pageable pageable) {
        return getAllFeeds(pageable, null);
    }

    private FeedEntity getFeedEntityOrThrow(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
    }

    private UserEntity getUserEntityOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }


    @Override
    public FeedDTO convertToDTO(FeedEntity feed, Long currentUserId) {
        boolean liked = currentUserId != null &&
                feed.getLikedUsers().stream()
                        .anyMatch(user -> user.getUserId().equals(currentUserId));

        List<CommentDTO> commentDTOs = feed.getComments() != null ?
                feed.getComments().stream().map(comment -> CommentDTO.builder()
                        .commentId(comment.getCommentId())
                        .userId(comment.getUser() != null ? comment.getUser().getUserId() : null)
                        .username(comment.getUser() != null ? comment.getUser().getUsername() : "Unknown")
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .userProfilePic(comment.getUser() != null ? comment.getUser().getUserImg() : null)
                        .build()).collect(Collectors.toList())
                : List.of();

        return FeedDTO.builder()
                .feedId(feed.getFeedId())
                .userId(feed.getUser().getUserId())
                .username(feed.getUser().getUsername())
                .userProfilePic(feed.getUser().getUserImg())
                .imageUrl(feed.getImageUrl())
                .caption(feed.getCaption())
                .createdAt(feed.getCreatedAt())
                .likes(feed.getLikes())
                .liked(liked)
                .comments(commentDTOs)
                .build();
    }

    private FeedEntity convertToEntity(FeedDTO dto) {
        UserEntity user = getUserEntityOrThrow(dto.getUserId());
        return FeedEntity.builder()
                .user(user)
                .imageUrl(dto.getImageUrl())
                .caption(dto.getCaption())
                .likes(dto.getLikes())
                .createdAt(dto.getCreatedAt() != null ? dto.getCreatedAt() :
                        new Date().toInstant().atZone(TimeZone.getDefault().toZoneId()).toLocalDateTime())
                .build();
    }
}