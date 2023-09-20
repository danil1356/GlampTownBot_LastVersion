package com.example.glamptownbot.service.EntityService.Impl;

import com.example.glamptownbot.data.Entity.Breakfast;
import com.example.glamptownbot.data.Entity.ImgData;
import com.example.glamptownbot.data.Repository.BreakfastRepository;
import com.example.glamptownbot.service.EntityService.BreakfastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BreakfastServiceImpl implements BreakfastService {

    final BreakfastRepository breakfastRepository;

    @Autowired
    public BreakfastServiceImpl(BreakfastRepository breakfastRepository){
        this.breakfastRepository = breakfastRepository;
    }

    @Override
    public Breakfast getById(Long id) {
        Optional<Breakfast> breakfast = breakfastRepository.findById(id);
        if (breakfast.isEmpty()){
            return null;
        }
        return breakfast.get();
    }

    @Override
    public List<Breakfast> getAll() {
        List<Breakfast> breakfasts = breakfastRepository.findAll();
        return breakfasts;
    }

    @Override
    public void delete(Long id) {
        breakfastRepository.deleteById(id);
    }

    @Override
    public Breakfast save(Breakfast entity) {
        Breakfast breakfast = breakfastRepository.save(entity);
        return breakfast;
    }
}
