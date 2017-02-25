package com.robocubs4205.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Created by trevor on 2/13/17.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    public User(){}

    public User(long id){
        this.id = id;
    }
    public User(String email,String name){
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
