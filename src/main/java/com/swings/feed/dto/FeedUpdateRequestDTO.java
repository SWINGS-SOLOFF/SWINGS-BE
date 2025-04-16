package com.swings.feed.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

// 게시물 수정 요청시 사용용도
@Getter
@Setter
public class FeedUpdateRequestDTO {
	
	// 게시물 본문
    private String caption;
    
    // 이미지 파일
    private MultipartFile file;
}
