package com.example.glamptownbot.data.Repository;

import com.example.glamptownbot.data.Entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomsRepository extends JpaRepository<Rooms, Long> {
}