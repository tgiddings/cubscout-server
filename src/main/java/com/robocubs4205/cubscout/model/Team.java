package com.robocubs4205.cubscout.model;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Team implements Identifiable<Long> {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private int number;

    @ManyToOne(optional = false)
    private Game game;

    private String name;

    @OneToMany(mappedBy = "team")
    private Set<Robot> robots;

    public Team() {
    }

    public Team(int number) {
        this.number = number;
    }

    @NotNull
    public Set<Robot> getRobots() {
        //nullRobotsToEmpty();
        return robots;
    }

    public void setRobots(Set<Robot> robots) {
        //nullRobotsToEmpty();
        this.robots = robots;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /*@PrePersist
    @PreUpdate
    private void nullRobotsToEmpty() {
        if (robots == null) robots = new HashSet<>();
    }*/

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
