package com.example.glamptownbot.data.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "add_service")
@Setter
@Getter


public class AddService extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private String price;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "addService")
    private Set<ImgData> urls;

    public AddService() {
    }

    public AddService(Long id) {
        super(id);
    }

    public AddService(Long id, String name, String description, String price) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
