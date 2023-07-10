package com.chatapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapplication.model.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

}
