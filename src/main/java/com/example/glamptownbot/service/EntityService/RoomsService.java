package com.example.glamptownbot.service.EntityService;

import com.example.glamptownbot.data.Entity.Rooms;

import java.util.List;

public interface RoomsService {
    Rooms getById(Long id);
    List<Rooms> getAll();
    void delete(Long id);
    Rooms save(Rooms entity);
}
