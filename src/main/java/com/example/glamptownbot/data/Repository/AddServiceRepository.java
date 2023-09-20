package com.example.glamptownbot.data.Repository;

import com.example.glamptownbot.data.Entity.AddService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddServiceRepository extends JpaRepository<AddService, Long> {
    AddService findByName(String desc);
}