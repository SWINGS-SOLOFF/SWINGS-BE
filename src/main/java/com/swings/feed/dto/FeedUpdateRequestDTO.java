package com.swings.feed.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FeedUpdateRequestDTO {
    private String caption;
    private MultipartFile file;
}
