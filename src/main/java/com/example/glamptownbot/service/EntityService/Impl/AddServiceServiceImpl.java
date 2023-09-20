package com.example.glamptownbot.service.EntityService.Impl;

import com.example.glamptownbot.data.Entity.AddService;
import com.example.glamptownbot.data.Entity.Breakfast;
import com.example.glamptownbot.data.Repository.AddServiceRepository;
import com.example.glamptownbot.service.EntityService.AddServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddServiceServiceImpl implements AddServiceService {

    final AddServiceRepository addServiceRepository;

    @Autowired
    public AddServiceServiceImpl(AddServiceRepository addServiceRepository){
        this.addServiceRepository = addServiceRepository;
    }

    @Override
    public AddService getById(Long id) {
        Optional<AddService> addService = addServiceRepository.findById(id);
        if (addService.isEmpty()){
            return null;
        }
        return addService.get();
    }

    @Override
    public List<AddService> getAll() {
        List<AddService> addServices = addServiceRepository.findAll();
        return addServices;
    }

    @Override
    public void delete(Long id) {
        addServiceRepository.deleteById(id);
    }

    @Override
    public AddService save(AddService entity) {
        AddService addService = addServiceRepository.save(entity);
        return addService;
    }

    @Override
    public AddService findByName(String name){
        AddService addService = addServiceRepository.findByName(name);
        return addService;
    }
}
