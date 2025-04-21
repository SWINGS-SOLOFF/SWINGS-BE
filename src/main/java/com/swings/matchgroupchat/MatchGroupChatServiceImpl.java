package com.swings.matchgroupchat;

import com.swings.chat.dto.ChatMessageDTO;
import com.swings.matchgroup.entity.MatchGroupEntity;
import com.swings.matchgroup.repository.MatchGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchGroupChatServiceImpl implements MatchGroupChatService {

    private final MatchGroupRepository matchGroupRepository;
    private final MatchGroupChatRepository matchGroupChatRepository;

    @Override
    public void save(ChatMessageDTO dto){
        MatchGroupEntity group = matchGroupRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("매칭 그룹이 존재하지 않습니다."));

        MatchGroupChatEntity message = MatchGroupChatEntity.builder()
                .matchGroup(group)
                .sender(dto.getSender())
                .content(dto.getContent())
                .sentAt(dto.getSentAt() != null ? dto.getSentAt() : java.time.LocalDateTime.now())
                .isRead(false)
                .build();

        matchGroupChatRepository.save(message);
    }

    @Override
    public List<ChatMessageDTO> getMessages(Long matchGroupId) {
        return matchGroupChatRepository.findByMatchGroup_MatchGroupIdOrderBySentAt(matchGroupId)
                .stream()
                .map(entity -> ChatMessageDTO.builder()
                        .roomId(entity.getMatchGroup().getMatchGroupId())
                        .sender(entity.getSender())
                        .content(entity.getContent())
                        .sentAt(entity.getSentAt())
                        .senderName(null) // 💡 닉네임 또는 표시 이름이 있다면 여기에 세팅 가능
                        .build()
                )
                .toList(); // Java 16 이상. 낮은 버전이면 .collect(Collectors.toList())
    }
}