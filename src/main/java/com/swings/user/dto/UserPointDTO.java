package com.swings.user.dto;

import com.swings.user.entity.UserPointEntity.PointType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserPointDTO {
    private Long userId;
    private int amount;
    private PointType type;
    private String description;
    private LocalDateTime createdAt;
}
