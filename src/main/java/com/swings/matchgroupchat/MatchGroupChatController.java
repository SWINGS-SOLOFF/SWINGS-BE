package com.swings.matchgroupchat;

import com.swings.chat.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/matchgroupchat")
@RequiredArgsConstructor
public class MatchGroupChatController {

    private final RedisPublisher redisPublisher;
    private final MatchGroupChatService matchGroupChatService;

    @MessageMapping("/chat.send/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDTO chatMessage) {
        chatMessage.setRoomId(roomId);
        redisPublisher.publish(roomId, chatMessage);
    }

    @GetMapping("/{matchGroupId}")
    public List<ChatMessageDTO> getChatMessages(@PathVariable Long matchGroupId) {
        return matchGroupChatService.getMessages(matchGroupId);
    }
}
