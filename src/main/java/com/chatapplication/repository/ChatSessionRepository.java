package com.chatapplication.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chatapplication.model.ChatSession;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Integer> {
    @Query(value = "SELECT c FROM ChatSession c WHERE c.user.username = :username")
    List<ChatSession> findByUsername(@Param("username") String username);
}
