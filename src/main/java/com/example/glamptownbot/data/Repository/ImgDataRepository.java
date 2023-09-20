package com.example.glamptownbot.data.Repository;

import com.example.glamptownbot.data.Entity.ImgData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImgDataRepository extends JpaRepository<ImgData, Long> {
    ImgData findByDescription(String description);
}