package com.example.glamptownbot.data.Repository;

import com.example.glamptownbot.data.Entity.Breakfast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreakfastRepository extends JpaRepository<Breakfast, Long> {
}