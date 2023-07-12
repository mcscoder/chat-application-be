package com.chatapplication.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketMessageRequest {
    private String senderUsername;
    private String recipientUsername;
    private String text;
    private int conversationId;
}
