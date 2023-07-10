package com.chatapplication.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userOneId;
    private Integer userTwoId;

    @OneToMany(mappedBy = "conversation")
    @JsonManagedReference
    private List<ChatSession> chatSessions;

    @OneToMany(mappedBy = "conversation")
    @JsonManagedReference
    private List<Message> messages;
}
