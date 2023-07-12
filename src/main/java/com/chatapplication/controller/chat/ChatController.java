package com.chatapplication.controller.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chatapplication.dto.chat.MessageDTO;
import com.chatapplication.dto.chat.WebsocketMessageRequest;
import com.chatapplication.model.Conversation;
import com.chatapplication.model.Message;
import com.chatapplication.model.User;
import com.chatapplication.repository.ChatSessionRepository;
import com.chatapplication.repository.ConversationRepository;
import com.chatapplication.repository.MessageRepository;
import com.chatapplication.repository.UserRepository;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @MessageMapping("/private-message")
    public void privateMessage(@Payload WebsocketMessageRequest request) {
        String recipientUsername = request.getRecipientUsername();
        MessageDTO messageDTO = MessageDTO.builder()
                .senderUsername(request.getSenderUsername())
                .recipientUsername(request.getRecipientUsername())
                .text(request.getText())
                .build();
        simpMessagingTemplate.convertAndSendToUser(recipientUsername, "/private", messageDTO);

        // Database logic
        User user = userRepository.findFirstByUsername(request.getSenderUsername());
        Conversation conversation = conversationRepository.findFirstById(request.getConversationId());
        Message message = Message.builder()
                .conversation(conversation)
                .sender(user)
                .text(request.getText())
                .build();
        messageRepository.save(message);
    }
}
