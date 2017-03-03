package com.robocubs4205.cubscout.model;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 2/14/17.
 */
@Entity
public class Robot implements Identifiable<Long> {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne
    private Team team;

    @NotNull
    private int number;

    @NotNull
    private int year;

    private String name;
    @ManyToMany(mappedBy = "robots")
    private List<Match> matches;

    public Robot(){}

    public Robot(long id){
        setId(id);
    }

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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    @PrePersist
    @PreUpdate
    private void nullMatchesToEmptyList(){
        if(matches==null) matches = new ArrayList<>();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
