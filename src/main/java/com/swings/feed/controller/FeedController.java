package com.swings.feed.controller;

import com.swings.feed.dto.FeedDTO;
import com.swings.feed.dto.CommentDTO;
import com.swings.feed.entity.CommentEntity;
import com.swings.feed.service.CommentService;
import com.swings.feed.service.FeedService;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public FeedController(FeedService feedService, UserRepository userRepository, CommentService commentService) {
        this.feedService = feedService;
        this.userRepository = userRepository;
        this.commentService = commentService;
    }

    // 피드 생성 (DTO 사용)
    @PostMapping
    public ResponseEntity<FeedDTO> createFeed(@RequestBody FeedDTO feedDTO) {
        FeedDTO createdFeedDTO = feedService.createFeed(feedDTO);
        return ResponseEntity.ok(createdFeedDTO);
    }

    // 전체 피드 조회 (DTO 리스트 반환)
    @GetMapping
    public ResponseEntity<List<FeedDTO>> getAllFeeds() {
        List<FeedDTO> feedDTOs = feedService.getAllFeeds();
        return ResponseEntity.ok(feedDTOs);
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
            File destFile = new File(filePath);
            file.transferTo(destFile);
            return "http://localhost:8090/swings/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }
    }

    // 저장된 파일 제공 (Static Resource 처리)
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) throws IOException {
        Path path = Paths.get("C:/uploads/" + fileName);
        byte[] data = Files.readAllBytes(path);
        return ResponseEntity.ok().body(data);
    }

    // 좋아요 증가
    @PutMapping("/{feedId}/like")
    public ResponseEntity<FeedDTO> likeFeed(@PathVariable Long feedId) {
        FeedDTO updatedFeedDTO = feedService.likeFeed(feedId);
        return ResponseEntity.ok(updatedFeedDTO);
    }

    // 좋아요 감소
    @PutMapping("/{feedId}/unlike")
    public ResponseEntity<FeedDTO> unlikeFeed(@PathVariable Long feedId) {
        FeedDTO updatedFeedDTO = feedService.unlikeFeed(feedId);
        return ResponseEntity.ok(updatedFeedDTO);
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
}