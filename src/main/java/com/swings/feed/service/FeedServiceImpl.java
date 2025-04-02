package com.swings.feed.service;

import com.swings.feed.dto.CommentDTO;
import com.swings.feed.dto.FeedDTO;
import com.swings.feed.entity.FeedEntity;
import com.swings.feed.repository.FeedRepository;
import com.swings.feed.service.FeedService;
import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedServiceImpl implements FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public FeedServiceImpl(FeedRepository feedRepository, UserRepository userRepository) {
        this.feedRepository = feedRepository;
        this.userRepository = userRepository;
    }

    @Override
    public FeedDTO createFeed(FeedDTO feedDTO) {
        FeedEntity feedEntity = feedDTOToEntity(feedDTO);
        FeedEntity savedFeed = feedRepository.save(feedEntity);
        return feedEntityToDTO(savedFeed);
    }

    @Override
    public List<FeedDTO> getAllFeeds(Long userId) {
        List<FeedEntity> feeds = feedRepository.findAll();
        UserEntity currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return feeds.stream().map(feed -> {
            boolean liked = feed.getLikedUsers().contains(currentUser);
            return new FeedDTO(feed, liked);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<FeedDTO> getFeedById(Long feedId) {
        return feedRepository.findById(feedId).map(this::feedEntityToDTO);
    }

    @Override
    public FeedDTO updateFeed(Long feedId, FeedDTO updatedFeedDTO) {
        return feedRepository.findById(feedId).map(feed -> {
            feed.setCaption(updatedFeedDTO.getCaption());
            feed.setImageUrl(updatedFeedDTO.getImageUrl());
            FeedEntity updatedFeed = feedRepository.save(feed);
            return feedEntityToDTO(updatedFeed);
        }).orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
    }

    @Override
    public void deleteFeed(Long feedId) {
        feedRepository.deleteById(feedId);
    }

    @Override
    @Transactional
    public FeedDTO likeFeed(Long feedId, Long userId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!feed.getLikedUsers().contains(user)) {
            feed.getLikedUsers().add(user);
            feed.setLikes(feed.getLikes() + 1);
        }

        FeedEntity updatedFeed = feedRepository.save(feed);
        return new FeedDTO(updatedFeed, true);
    }

    @Override
    @Transactional
    public FeedDTO unlikeFeed(Long feedId, Long userId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (feed.getLikedUsers().contains(user)) {
            feed.getLikedUsers().remove(user);
            feed.setLikes(Math.max(0, feed.getLikes() - 1));
        }

        FeedEntity updatedFeed = feedRepository.save(feed);
        return new FeedDTO(updatedFeed, false);
    }

    @Override
    public int getUserFeedCount(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        return feedRepository.countByUser_UserId(userId);
    }

    @Override
    @Transactional
    public List<FeedDTO> getFeedsByUserId(Long userId) {
        List<FeedEntity> feeds = feedRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
        return feeds.stream().map(this::feedEntityToDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<UserDTO> getLikedUsers(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
        return feed.getLikedUsers().stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setUsername(user.getUsername());
            userDTO.setUserImg(user.getUserImg() != null ? user.getUserImg() : "default-image-url");
            return userDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<FeedDTO> getFeedsRandomized(Long userId, Pageable pageable) {
        Page<FeedEntity> feedPage = feedRepository.findByUser_UserIdOrderByCreatedAtDesc(userId, pageable);
        List<FeedEntity> feeds = new ArrayList<>(feedPage.getContent());
        Collections.shuffle(feeds);
        return feeds.stream().map(this::feedEntityToDTO).collect(Collectors.toList());
    }

    private FeedEntity feedDTOToEntity(FeedDTO feedDTO) {
        FeedEntity feedEntity = new FeedEntity();
        UserEntity user = userRepository.findById(feedDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + feedDTO.getUserId()));
        feedEntity.setUser(user);
        feedEntity.setImageUrl(feedDTO.getImageUrl());
        feedEntity.setCaption(feedDTO.getCaption());
        feedEntity.setLikes(feedDTO.getLikes());
        feedEntity.setCreatedAt(feedDTO.getCreatedAt());
        return feedEntity;
    }

    private FeedDTO feedEntityToDTO(FeedEntity feedEntity) {
        return FeedDTO.builder()
                .feedId(feedEntity.getFeedId())
                .userId(feedEntity.getUser().getUserId())
                .username(feedEntity.getUser().getUsername())
                .imageUrl(feedEntity.getImageUrl())
                .caption(feedEntity.getCaption())
                .createdAt(feedEntity.getCreatedAt())
                .likes(feedEntity.getLikes())
                .liked(false)
                .comments(feedEntity.getComments() != null ? feedEntity.getComments().stream()
                        .map(commentEntity -> new CommentDTO(
                                commentEntity.getCommentId(),
                                commentEntity.getUser() != null ? commentEntity.getUser().getUserId() : null,
                                commentEntity.getUser() != null ? commentEntity.getUser().getUsername() : "Unknown User",
                                commentEntity.getContent(),
                                commentEntity.getCreatedAt()))
                        .collect(Collectors.toList()) : List.of())
                .build();
    }
}
