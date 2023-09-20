package com.example.glamptownbot.data.Repository;

import com.example.glamptownbot.data.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByChatId(String chatId);
}