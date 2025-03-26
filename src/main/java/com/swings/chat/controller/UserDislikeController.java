package com.swings.chat.controller;


import com.swings.chat.service.UserDislikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dislikes")
@RequiredArgsConstructor
public class UserDislikeController {

    private final UserDislikeService userDislikeService;

    @PostMapping("/{fromUsername}/{toUsername}")
    public ResponseEntity<String> dislikeUser(@PathVariable String fromUsername, @PathVariable String toUsername) {
        userDislikeService.dislikeUser(fromUsername, toUsername);
        return ResponseEntity.ok("싫어요를 눌렀습니다.");
    }
}