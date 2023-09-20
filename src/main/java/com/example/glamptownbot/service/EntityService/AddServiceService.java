package com.example.glamptownbot.service.EntityService;


import com.example.glamptownbot.data.Entity.AddService;

import java.util.List;

public interface AddServiceService {

    AddService getById(Long id);
    List<AddService> getAll();
    void delete(Long id);
    AddService save(AddService entity);

    AddService findByName(String name);
}
