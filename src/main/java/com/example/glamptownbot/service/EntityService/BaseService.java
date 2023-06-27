package com.example.glamptownbot.service.EntityService;

import java.util.List;

public interface BaseService <T>{

    T getById(Long id);
    List<T> getAll();
    void delete(Long id);
    T save(T entity);

}
