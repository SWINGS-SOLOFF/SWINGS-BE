package com.swings.feed.controller;

import com.swings.feed.entity.CommentEntity;
import com.swings.feed.entity.FeedEntity;
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

    // 피드 생성
    @PostMapping
    public ResponseEntity<FeedEntity> createFeed(@RequestBody FeedEntity feed) {
        FeedEntity createdFeed = feedService.createFeed(feed);
        return ResponseEntity.ok(createdFeed);
    }

    // 전체 피드 조회
    @GetMapping
    public ResponseEntity<List<FeedEntity>> getAllFeeds() {
        List<FeedEntity> feeds = feedService.getAllFeeds();
        return ResponseEntity.ok(feeds);
    }

    // 특정 피드 조회
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedEntity> getFeedById(@PathVariable Long feedId) {
        return feedService.getFeedById(feedId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 피드 수정
    @PutMapping("/{feedId}")
    public ResponseEntity<FeedEntity> updateFeed(@PathVariable Long feedId,
                                           @RequestBody FeedEntity updatedFeed) {
        FeedEntity feed = feedService.updateFeed(feedId, updatedFeed);
        return ResponseEntity.ok(feed);
    }

    // 피드 삭제
    @DeleteMapping("/{feedId}")
    public ResponseEntity<Void> deleteFeed(@PathVariable Long feedId) {
        feedService.deleteFeed(feedId);
        return ResponseEntity.noContent().build();
    }

    // 파일 업로드와 함께 피드 생성
    @PostMapping("/upload")
    public ResponseEntity<FeedEntity> uploadFeed(
            @RequestParam("userId") Long userId,
            @RequestParam("content") String content,
            @RequestParam("file") MultipartFile file
    ) {
        // 파일 저장 (로컬 저장소 예시)
        String savedUrl = saveFile(file);

        // userId로 User 조회
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Feed 엔티티 생성 및 저장 (Lombok Builder 사용)
        FeedEntity feed = FeedEntity.builder()
                .user(user)
                .caption(content)
                .imageUrl(savedUrl)
                .build();

        FeedEntity created = feedService.createFeed(feed);
        return ResponseEntity.ok(created);
    }

    // 파일 저장 메소드 예시 (로컬 디스크에 저장)
    private String saveFile(MultipartFile file) {
        String uploadDir = "C:/uploads/";  // 저장 폴더 (프로젝트 루트에 생성하거나, 절대 경로 사용)
        // 파일 이름을 고유하게 생성 (UUID 사용)
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 저장 경로
        String filePath = uploadDir + fileName;
        try {
            // 파일 저장
            File destFile = new File(filePath);
            file.transferTo(destFile);  // 파일을 지정한 경로에 저장
            // URL 출력
            String savedUrl = "http://localhost:8090/swings/uploads/" + fileName;
            System.out.println("Saved URL: " + savedUrl);  // 여기서 URL을 출력합니다.
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed: " + e.getMessage());
        }

        // 저장된 파일의 URL 반환 (서버에서 접근할 수 있는 URL)
        return "http://localhost:8090/swings/uploads/" + fileName;  // 로컬 서버에서 접근 가능한 URL 반환

    }

    // 파일을 제공할 수 있는 URL을 처리할 수 있도록 설정 (Spring Boot의 Static Resource 처리)
    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<byte[]> getFile(@PathVariable String fileName) throws IOException {
        Path path = Paths.get("C:/uploads/" + fileName);  // 로컬 경로에서 파일 찾기
        byte[] data = Files.readAllBytes(path);
        return ResponseEntity.ok().body(data);
    }

    @PutMapping("/{feedId}/like")
    public ResponseEntity<FeedEntity> likeFeed(@PathVariable Long feedId) {
        FeedEntity updatedFeed = feedService.likeFeed(feedId);
        return ResponseEntity.ok(updatedFeed);
    }

    @PutMapping("/{feedId}/unlike")
    public ResponseEntity<FeedEntity> unlikeFeed(@PathVariable Long feedId) {
        FeedEntity updatedFeed = feedService.unlikeFeed(feedId);
        return ResponseEntity.ok(updatedFeed);
    }

    // 댓글 추가
    @PostMapping("/{feedId}/comments")
    public ResponseEntity<CommentEntity> addComment(@PathVariable Long feedId,
                                                    @RequestParam Long userId,
                                                    @RequestParam String content) {
        CommentEntity comment = commentService.addComment(feedId, userId, content);
        return ResponseEntity.ok(comment);
    }

    // 댓글 삭제
    @DeleteMapping("/{feedId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long feedId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }



    // 특정 피드의 댓글 조회
    @GetMapping("/{feedId}/comments")
    public ResponseEntity<List<CommentEntity>> getCommentsByFeedId(@PathVariable Long feedId) {
        List<CommentEntity> comments = commentService.getCommentsByFeedId(feedId);
        return ResponseEntity.ok(comments);
    }

}


