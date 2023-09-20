package com.example.glamptownbot.service.EntityService.Impl;

import com.example.glamptownbot.data.Entity.ImgData;
import com.example.glamptownbot.data.Entity.Rooms;
import com.example.glamptownbot.data.Repository.ImgDataRepository;
import com.example.glamptownbot.data.Repository.RoomsRepository;
import com.example.glamptownbot.service.EntityService.ImgDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImgDataServiceImpl implements ImgDataService {

    final ImgDataRepository imgDataRepository;

    @Autowired
    public ImgDataServiceImpl(ImgDataRepository imgDataRepository){
        this.imgDataRepository = imgDataRepository;
    }


    @Override
    public ImgData getById(Long id) {
        Optional<ImgData> imgData = imgDataRepository.findById(id);
        if (imgData.isEmpty()){
            return null;
        }
        return imgData.get();
    }

    @Override
    public List<ImgData> getAll() {
        List<ImgData> imgData = imgDataRepository.findAll();
        return imgData;
    }

    @Override
    public void delete(Long id) {
        imgDataRepository.deleteById(id);
    }

    @Override
    public ImgData save(ImgData entity) {
        ImgData imgData = imgDataRepository.save(entity);
        return imgData;
    }

    @Override
    public ImgData findByDescription(String description){
        return imgDataRepository.findByDescription(description);
    }
}
