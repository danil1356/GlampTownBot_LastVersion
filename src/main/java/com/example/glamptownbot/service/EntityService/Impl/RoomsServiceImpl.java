package com.example.glamptownbot.service.EntityService.Impl;

import com.example.glamptownbot.data.Entity.Rooms;
import com.example.glamptownbot.data.Repository.RoomsRepository;
import com.example.glamptownbot.service.EntityService.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomsServiceImpl implements RoomsService {

    final RoomsRepository roomsRepository;

    @Autowired
    public RoomsServiceImpl(RoomsRepository roomsRepository){
        this.roomsRepository = roomsRepository;
    }


    @Override
    public Rooms getById(Long id) {
        Optional<Rooms> rooms = roomsRepository.findById(id);
        if (rooms.isEmpty()){
            return null;
        }
        return rooms.get();
    }

    @Override
    public List<Rooms> getAll() {
        List<Rooms> rooms = roomsRepository.findAll();
        return rooms;
    }

    @Override
    public void delete(Long id) {
        roomsRepository.deleteById(id);
    }

    @Override
    public Rooms save(Rooms entity) {
        Rooms rooms = roomsRepository.save(entity);
        return rooms;
    }

}
