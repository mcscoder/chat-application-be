package com.chatapplication.controller.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatapplication.dto.chat.AddChatSessionRequest;
import com.chatapplication.dto.chat.ChatSessionDTO;
import com.chatapplication.dto.chat.GetConversationRequest;
import com.chatapplication.dto.chat.MessageDTO;
import com.chatapplication.model.ChatSession;
import com.chatapplication.model.Conversation;
import com.chatapplication.model.Message;
import com.chatapplication.model.User;
import com.chatapplication.repository.ChatSessionRepository;
import com.chatapplication.repository.ConversationRepository;
import com.chatapplication.repository.UserRepository;
import com.chatapplication.security.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @GetMapping("/get-all-chat-session")
    public List<ChatSessionDTO> getAllChatSession(
            HttpServletRequest request) {

        List<ChatSessionDTO> chatSessionDTOs = new ArrayList<>();

        String senderUsername = jwtUtils.extractUsername(request);
        List<ChatSession> chatSessions = chatSessionRepository.findByUsername(senderUsername);

        for (ChatSession chatSession : chatSessions) {
            String user1 = chatSession.getConversation().getUsername1();
            String user2 = chatSession.getConversation().getUsername2();
            Integer conversationId = chatSession.getConversation().getId();

            if (user1.equals(senderUsername)) {
                chatSessionDTOs.add(ChatSessionDTO.builder()
                        .senderUsername(user1)
                        .recipientUsername(user2)
                        .conversationId(conversationId)
                        .build());
            } else {
                chatSessionDTOs.add(ChatSessionDTO.builder()
                        .senderUsername(user2)
                        .recipientUsername(user1)
                        .conversationId(conversationId)
                        .build());
            }
        }
        return chatSessionDTOs;
    }

    @PostMapping("/get-conversation")
    public List<MessageDTO> getConversation(
            HttpServletRequest request,
            @RequestBody GetConversationRequest getConversationRequest) {

        int conversationId = getConversationRequest.getConversationId();
        Conversation conversation = conversationRepository.findFirstById(conversationId);

        List<MessageDTO> messageDTOs = new ArrayList<>();
        List<Message> messages = conversation.getMessages();
        for (Message message : messages) {
            String text = message.getText();
            String senderUsername = message.getSender().getUsername();
            messageDTOs.add(MessageDTO.builder()
                    .text(text)
                    .senderUsername(senderUsername)
                    .build());
        }

        return messageDTOs;
    }

    @PostMapping("/add-chat-session")
    public ChatSessionDTO addChatSession(
            HttpServletRequest request,
            @RequestBody AddChatSessionRequest addChatSessionRequest) {

        String username1 = jwtUtils.extractUsername(request);
        String username2 = addChatSessionRequest.getNewUsername();

        Conversation conversation = Conversation.builder()
                .username1(username1)
                .username2(username2)
                .build();

        Conversation newConversation = conversationRepository.save(conversation);

        User user1 = userRepository.findFirstByUsername(username1);
        User user2 = userRepository.findFirstByUsername(username2);
        if (user2 == null) {
            throw new UsernameNotFoundException("User is not exist");
        }

        ChatSession chatSession1 = ChatSession.builder()
                .conversation(newConversation)
                .user(user1)
                .build();
        chatSessionRepository.save(chatSession1);

        ChatSession chatSession2 = ChatSession.builder()
                .conversation(newConversation)
                .user(user2)
                .build();
        chatSessionRepository.save(chatSession2);

        ChatSessionDTO chatSessionDTO = ChatSessionDTO.builder()
                .senderUsername(username1)
                .recipientUsername(username2)
                .conversationId(newConversation.getId())
                .build();
        return chatSessionDTO;
    }
}
