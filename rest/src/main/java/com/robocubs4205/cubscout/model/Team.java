package com.robocubs4205.cubscout.model;

import org.springframework.hateoas.Identifiable;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


public class Team implements Identifiable<Long>{

    private long id;

    @NotNull
    private int number;

    private String name;

    @NotNull
    private String gameType;

    private District district;

    private Set<Robot> robots = new HashSet<>();

    public Team() {
    }

    public Team(int number) {
        this.number = number;
    }

    public Set<Robot> getRobots() {
        return robots;
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

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }
}
