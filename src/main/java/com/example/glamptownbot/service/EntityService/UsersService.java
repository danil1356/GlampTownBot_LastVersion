package com.example.glamptownbot.service.EntityService;

import com.example.glamptownbot.data.Entity.Users;

import java.util.List;

public interface UsersService{
    Users getById(Long id);
    List<Users> getAll();
    void delete(Long id);
    Users save(Users entity);
    Users findByChatId(String chatId);
}
