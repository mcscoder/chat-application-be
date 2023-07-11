package com.chatapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapplication.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {
    Conversation findFirstById(int id);
}
