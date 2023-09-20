package com.example.glamptownbot.data.Entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "users")
@Setter
@Getter


public class Users extends BaseEntity {

    @Column(name = "chat_id")
    private String chatId;

    @Column(name = "first_name")
    private String first_name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "phone_number")
    private String phone_number;

    @Column(name = "register_time")
    private Timestamp register_time;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Rooms> rooms;

    public Users(){
    }

    public Users(Long id) {
        super(id);
    }

    public Users(Long id, String chat_id, String first_name, String last_name, String phone_number, Timestamp register_time) {
        super(id);
        this.chatId = chat_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.register_time = register_time;
    }
}
