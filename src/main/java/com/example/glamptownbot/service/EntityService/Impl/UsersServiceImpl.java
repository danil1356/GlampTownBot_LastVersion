package com.example.glamptownbot.service.EntityService.Impl;

import com.example.glamptownbot.data.Entity.Users;
import com.example.glamptownbot.data.Repository.UsersRepository;
import com.example.glamptownbot.service.EntityService.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService {

    final UsersRepository usersRepository;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository){
        this.usersRepository = usersRepository;
    }


    @Override
    public Users getById(Long id) {
        Optional<Users> users = usersRepository.findById(id);
        if (users.isEmpty()){
            return null;
        }
        return users.get();
    }

    @Override
    public List<Users> getAll() {
        List<Users> users = usersRepository.findAll();
        return users;
    }

    @Override
    public void delete(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public Users save(Users entity) {
        Users users = usersRepository.save(entity);
        return users;
    }

    @Override
    public Users findByChatId(String chatId) {
        Users user = usersRepository.findByChatId(chatId);
        return user;
    }
}
