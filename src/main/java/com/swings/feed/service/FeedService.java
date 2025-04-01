package com.swings.feed.service;

import com.swings.feed.dto.CommentDTO;
import com.swings.feed.dto.FeedDTO;
import com.swings.feed.entity.FeedEntity;
import com.swings.feed.repository.FeedRepository;
import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeedService {

    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    public FeedService(FeedRepository feedRepository, UserRepository userRepository) {
        this.feedRepository = feedRepository;
        this.userRepository = userRepository;
    }

    // 피드 작성
    public FeedDTO createFeed(FeedDTO feedDTO) {
        FeedEntity feedEntity = feedDTOToEntity(feedDTO);
        FeedEntity savedFeed = feedRepository.save(feedEntity);
        return feedEntityToDTO(savedFeed);
    }

    // 전체 피드 조회 – userId를 전달하여 현재 사용자의 좋아요 여부를 계산
    public List<FeedDTO> getAllFeeds(Long userId) {
        List<FeedEntity> feeds = feedRepository.findAll();
        UserEntity currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return feeds.stream()
                .map(feed -> {
                    boolean liked = feed.getLikedUsers().contains(currentUser);
                    return new FeedDTO(feed, liked);
                })
                .collect(Collectors.toList());
    }

    // 특정 피드 조회
    @Transactional
    public Optional<FeedDTO> getFeedById(Long feedId) {
        return feedRepository.findById(feedId)
                .map(this::feedEntityToDTO);
    }

    // 피드 수정
    public FeedDTO updateFeed(Long feedId, FeedDTO updatedFeedDTO) {
        return feedRepository.findById(feedId)
                .map(feed -> {
                    feed.setCaption(updatedFeedDTO.getCaption());
                    feed.setImageUrl(updatedFeedDTO.getImageUrl());
                    // user나 createdAt 등 변경 로직 필요 시 추가
                    FeedEntity updatedFeed = feedRepository.save(feed);
                    return feedEntityToDTO(updatedFeed);
                })
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
    }

    // 피드 삭제
    public void deleteFeed(Long feedId) {
        feedRepository.deleteById(feedId);
    }

    // 좋아요 증가
    @Transactional
    public FeedDTO likeFeed(Long feedId, Long userId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 사용자가 이미 좋아요를 눌렀다면 추가하지 않음
        if (!feed.getLikedUsers().contains(user)) {
            feed.getLikedUsers().add(user); // 사용자 추가
            feed.setLikes(feed.getLikes() + 1); // 좋아요 수 증가
        }

        FeedEntity updatedFeed = feedRepository.save(feed);
        return new FeedDTO(updatedFeed, true);
    }

    // 좋아요 취소
    @Transactional
    public FeedDTO unlikeFeed(Long feedId, Long userId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (feed.getLikedUsers().contains(user)) {
            feed.getLikedUsers().remove(user); // 사용자 제거
            feed.setLikes(Math.max(0, feed.getLikes() - 1)); // 좋아요 수 감소
        }

        FeedEntity updatedFeed = feedRepository.save(feed);
        return new FeedDTO(updatedFeed, false);
    }

    // FeedDTO -> FeedEntity 변환
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

    // FeedEntity -> FeedDTO 변환
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
                .comments(feedEntity.getComments() != null
                        ? feedEntity.getComments().stream()
                        .map(commentEntity -> new CommentDTO(
                                commentEntity.getCommentId(),
                                commentEntity.getUser() != null ? commentEntity.getUser().getUserId() : null,
                                commentEntity.getUser() != null ? commentEntity.getUser().getUsername() : "Unknown User",
                                commentEntity.getContent(),
                                commentEntity.getCreatedAt()
                        ))
                        .collect(Collectors.toList())
                        : List.of())
                .build();
    }

    // userId에 해당하는 피드의 개수를 반환하는 로직
    public int getUserFeedCount(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("UserId must not be null");
        }
        // userId에 해당하는 피드의 개수를 반환하는 로직
        return feedRepository.countByUser_UserId(userId);
    }

    // In FeedService.java

    /**
     * 특정 사용자의 피드만 조회
     * @param userId 사용자 ID
     * @return 사용자의 피드 목록
     */

    @Transactional
    public List<FeedDTO> getFeedsByUserId(Long userId) {
        List<FeedEntity> feeds = feedRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
        return feeds.stream()
                .map(this::feedEntityToDTO)
                .collect(Collectors.toList());
    }

    // 좋아요한 사용자 목록 가져오기
    @Transactional
    public List<UserDTO> getLikedUsers(Long feedId) {
        FeedEntity feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + feedId));

        return feed.getLikedUsers().stream()
                .map(user -> new UserDTO(
                        user.getUserId(),
                        user.getUsername(),
                        user.getUserImg() != null ? user.getUserImg() : "default-image-url"
                ))
                .collect(Collectors.toList());
    }

    // 페이징 및 랜덤 정렬된 피드 조회
    public List<FeedDTO> getFeedsRandomized(Long userId, Pageable pageable) {
        Page<FeedEntity> feedPage = feedRepository.findByUser_UserIdOrderByCreatedAtDesc(userId, pageable);
        List<FeedEntity> feeds = new ArrayList<>(feedPage.getContent());
        Collections.shuffle(feeds);
        return feeds.stream()
                .map(this::feedEntityToDTO)
                .collect(Collectors.toList());
    }

}
