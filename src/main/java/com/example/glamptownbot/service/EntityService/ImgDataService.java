package com.example.glamptownbot.service.EntityService;

import com.example.glamptownbot.data.Entity.ImgData;

import java.util.List;

public interface ImgDataService {
    ImgData getById(Long id);
    List<ImgData> getAll();
    void delete(Long id);
    ImgData save(ImgData entity);

    ImgData findByDescription(String description);
}
