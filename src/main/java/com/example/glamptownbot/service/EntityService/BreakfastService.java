package com.example.glamptownbot.service.EntityService;

import com.example.glamptownbot.data.Entity.Breakfast;

import java.util.List;

public interface BreakfastService {
    Breakfast getById(Long id);
    List<Breakfast> getAll();
    void delete(Long id);
    Breakfast save(Breakfast entity);
}
