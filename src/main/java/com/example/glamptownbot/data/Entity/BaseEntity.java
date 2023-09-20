package com.example.glamptownbot.data.Entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Setter
@Getter

public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public BaseEntity(Long id){
        this.id = id;

    }

    public BaseEntity(){
    }
}
