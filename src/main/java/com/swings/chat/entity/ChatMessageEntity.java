package com.swings.chat.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageEntity {

    private String message;
    private String sender;
    private String roomId;
    private MessageType type;

    public enum MessageType{
        ENTER, TALK, LEAVE
    }

}
