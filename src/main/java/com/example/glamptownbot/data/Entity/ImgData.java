package com.example.glamptownbot.data.Entity;

import javax.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "img_data")
@Setter
@Getter


public class ImgData extends BaseEntity {

    @Column(name = "url")
    private String url;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "rooms_id", referencedColumnName = "id")
    private Rooms room;

    @ManyToOne
    @JoinColumn(name = "breakfast_id", referencedColumnName = "id")
    private Breakfast breakfast;

    @ManyToOne
    @JoinColumn(name = "add_service_id", referencedColumnName = "id")
    private AddService addService;

    public ImgData(Long id) {
        super(id);
    }

    public ImgData() {
    }

    public ImgData(Long id, String url, String description, Rooms room, Breakfast breakfast) {
        super(id);
        this.url = url;
        this.description = description;
        this.room = room;
        this.breakfast = breakfast;
    }
}
