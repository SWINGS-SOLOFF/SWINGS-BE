package com.swings.social.dto;

import java.util.Objects;
import com.swings.user.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialDTO {
	
    // 팔로우, 언팔로우 관계 맺기에 필요한 사용자 아이디
    private Long followerId;
    private Long followeeId;
    
    // 변환된 UserDTO 객체
    private UserDTO follower;
    private UserDTO followee;
    
    // 두 SocialDTO 객체는 followerId와 followeeId가 같으면 동등하다고 판단
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SocialDTO socialDTO = (SocialDTO) o;
        return Objects.equals(followerId, socialDTO.followerId) &&
               Objects.equals(followeeId, socialDTO.followeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followeeId);
    }
}
