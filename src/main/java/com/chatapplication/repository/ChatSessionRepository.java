package com.chatapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapplication.model.ChatSession;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Integer> {

}
