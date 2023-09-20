package com.example.glamptownbot.data.Entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "breakfast")
@Setter
@Getter
public class Breakfast extends BaseEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private String price;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "breakfast")
    private Set<ImgData> imgData;

    public Breakfast() {
    }

    public Breakfast(Long id) {
        super(id);
    }

    public Breakfast(Long id, String name, String description, String price, Set<ImgData> imgData) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgData = imgData;
    }
}
