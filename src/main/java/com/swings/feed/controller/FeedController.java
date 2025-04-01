package com.swings.feed.controller;

import com.swings.feed.dto.FeedDTO;
import com.swings.feed.dto.CommentDTO;
import com.swings.feed.entity.CommentEntity;
import com.swings.feed.entity.FeedEntity;
import com.swings.feed.repository.FeedRepository;
import com.swings.feed.service.CommentService;
import com.swings.feed.service.FeedService;
import com.swings.user.dto.UserDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.Getter;
import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/feeds")
public class FeedController {

    private final FeedService feedService;
    private final UserRepository userRepository;
    private final CommentService commentService;
    private final FeedRepository feedRepository;

    public FeedController(FeedService feedService, UserRepository userRepository, CommentService commentService, FeedRepository feedRepository) {
        this.feedService = feedService;
        this.userRepository = userRepository;
        this.commentService = commentService;
        this.feedRepository = feedRepository;
    }

    // 피드 생성 (DTO 사용)
    @PostMapping
    public ResponseEntity<FeedDTO> createFeed(@RequestBody FeedDTO feedDTO) {
        FeedDTO createdFeedDTO = feedService.createFeed(feedDTO);
        return ResponseEntity.ok(createdFeedDTO);
    }

    // 전체 피드 조회 (DTO 리스트 반환)
    @GetMapping
    public ResponseEntity<List<FeedDTO>> getFeeds(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<FeedDTO> feeds = feedService.getFeedsRandomized(userId, pageable);

        return ResponseEntity.ok(feeds != null ? feeds : List.of());
    }

    // 특정 유저의 피드만 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FeedDTO>> getFeedsByUserId(@PathVariable Long userId) {
        List<FeedDTO> feedDTOs = feedService.getFeedsByUserId(userId);
        return ResponseEntity.ok(feedDTOs);
    }

    // 특정 피드 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedDTO> getFeedById(@PathVariable Long feedId) {
        return feedService.getFeedById(feedId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 피드 수정
    @PutMapping("/{feedId}")
    public ResponseEntity<FeedDTO> updateFeed(@PathVariable Long feedId,
                                              @RequestBody FeedDTO updatedFeedDTO) {
        FeedDTO updatedFeed = feedService.updateFeed(feedId, updatedFeedDTO);
        return ResponseEntity.ok(updatedFeed);
    }

    // 피드 삭제
    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.noContent().build();
    }

    // 파일 업로드와 함께 피드 생성 (DTO 사용)
    @PostMapping("/upload")
    public ResponseEntity<FeedDTO> uploadFeed(
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("file") MultipartFile file
    ) {
        String savedUrl = saveFile(file);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // DTO로 피드 정보 구성 (필요한 최소 정보만 설정)
        FeedDTO feedDTO = FeedDTO.builder()
                .userId(user.getUserId())
                .caption(content)
                .imageUrl(savedUrl)
                .build();

        FeedDTO createdFeedDTO = feedService.createFeed(feedDTO);
        return ResponseEntity.ok(createdFeedDTO);
    }

    // 파일 저장 메소드 (로컬 디스크에 저장)
    private String saveFile(MultipartFile file) {
        String uploadDir = "C:/uploads/";
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + fileName;
        try {
            // 리사이즈 처리 (최대 너비 800px, 최대 높이 600px)
            File destFile = new File(filePath);
            Thumbnails.of(file.getInputStream())
                    .size(800, 600)  // 크기 지정
                    .toFile(destFile);
            return "http://localhost:8090/swings/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    // 좋아요 증가
    @PutMapping("/{feedId}/like")
    public ResponseEntity<FeedDTO> likeFeed(@PathVariable Long feedId, @RequestParam Long userId) {
        FeedDTO updatedFeedDTO = feedService.likeFeed(feedId, userId);
        return ResponseEntity.ok(updatedFeedDTO);
    }

    // 좋아요 취소
    @PutMapping("/{feedId}/unlike")
    public ResponseEntity<?> unlikeFeed(@PathVariable Long feedId, @RequestParam Long userId) {
        if (feedId == null || userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Feed ID or User ID cannot be null");
        }

        feedService.unlikeFeed(feedId, userId);
        return ResponseEntity.ok("Feed unliked successfully");
    }

    // 댓글 추가
    @PostMapping("/{feedId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long feedId,
                                                 @RequestParam Long userId,
                                                 @RequestParam String content) {
        // commentService.addComment는 CommentEntity를 반환하므로 변환
        CommentEntity comment = commentService.addComment(feedId, userId, content);
        CommentDTO commentDTO = commentEntityToDTO(comment);
        return ResponseEntity.ok(commentDTO);
    }

    // 댓글 삭제
    @DeleteMapping("/{feedId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long feedId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    // 특정 피드의 댓글 조회
    @GetMapping("/{feedId}/comments")
    public ResponseEntity<?> getCommentsByFeedId(@PathVariable Long feedId) {
        try {
            List<CommentEntity> comments = commentService.getCommentsByFeedId(feedId);

            // 댓글이 없을 경우 빈 리스트 반환
            if (comments == null || comments.isEmpty()) {
                return ResponseEntity.ok(List.of());  // 빈 리스트 반환
            }

            // Entity -> DTO 변환
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(this::commentEntityToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(commentDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body("서버 내부 오류 발생: " + e.getMessage());
        }
    }

    // CommentEntity -> CommentDTO 변환 메소드
    private CommentDTO commentEntityToDTO(CommentEntity commentEntity) {
        return CommentDTO.builder()
                .commentId(commentEntity.getCommentId())
                .userId(commentEntity.getUser() != null ? commentEntity.getUser().getUserId() : null)
                .username(commentEntity.getUser() != null ? commentEntity.getUser().getUsername() : "Unknown User")
                .content(commentEntity.getContent())
                .createdAt(commentEntity.getCreatedAt())
                .build();
    }

    // 특정 사용자 정보 조회 (ID 기반)
    public UserEntity getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @GetMapping("/{feedId}/liked-users")
    public ResponseEntity<List<UserDTO>> getLikedUsers(@PathVariable Long feedId) {
        List<UserDTO> likedUsers = feedService.getLikedUsers(feedId);
        return ResponseEntity.ok(likedUsers);
    }

    // UserIdRequest 클래스 정의
    @Setter
    @Getter
    public class UserIdRequest {
        private Long userId;

    }

    @GetMapping("/feeds/randomized")
    public ResponseEntity<List<FeedDTO>> getFeedsRandomized(
            @RequestParam Long userId,
            @RequestParam int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FeedEntity> feedPage = feedRepository.findByUser_UserIdOrderByCreatedAtDesc(userId, pageable);
        List<FeedEntity> feeds = new ArrayList<>(feedPage.getContent());
        Collections.shuffle(feeds);

        List<FeedDTO> feedDTOs = feeds.stream()
                .map(this::feedEntityToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(feedDTOs);
    }

    private FeedDTO feedEntityToDTO(FeedEntity feedEntity) {
        FeedDTO feedDTO = new FeedDTO();
        feedDTO.setFeedId(feedEntity.getFeedId());
        feedDTO.setCreatedAt(feedEntity.getCreatedAt());

        List<CommentDTO> commentDTOs = feedEntity.getComments().stream()
                .map(this::commentEntityToDTO)
                .collect(Collectors.toList());
        feedDTO.setComments(commentDTOs);

        return feedDTO;
    }



}