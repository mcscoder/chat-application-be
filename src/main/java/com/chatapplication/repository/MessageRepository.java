package com.chatapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapplication.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

}
