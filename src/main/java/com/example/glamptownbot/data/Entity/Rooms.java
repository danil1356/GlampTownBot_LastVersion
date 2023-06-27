package com.example.glamptownbot.data.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rooms")
@Data
public class Rooms extends BaseEntity{

    @Column(name = "description")
    private String description;

    @Column(name = "free", nullable = false)
    private Boolean free;

    @Column(name = "price")
    private String price;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users user;


    public Rooms() {
    }

    public Rooms(Long id) {
        super(id);
    }

    public Rooms(Long id, String description, Boolean free, String price, Users user) {
        super(id);
        this.description = description;
        this.free = free;
        this.price = price;
        this.user = user;
    }
}
