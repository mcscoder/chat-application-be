package com.chatapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatapplication.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findFirstByUsername(String username);
}
