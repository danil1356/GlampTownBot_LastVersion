package com.example.glamptownbot.data.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "rooms")
@Setter
@Getter


public class Rooms extends BaseEntity{

    @Column(name = "description")
    private String description;

    @Column(name = "free", nullable = false)
    private Boolean free;

    @Column(name = "standard_price")
    private String standard_price;

    @Column(name = "weekend_price")
    private String weekend_price;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "room")
    private Set<ImgData> imgDataSet;


    public Rooms() {
    }

    public Rooms(Long id) {
        super(id);
    }

    public Rooms(Long id, String description, Boolean free, String standard_price, String weekend_price,String name, Users user) {
        super(id);
        this.description = description;
        this.free = free;
        this.standard_price = standard_price;
        this.weekend_price = weekend_price;
        this.name = name;
        this.user = user;
    }
}
