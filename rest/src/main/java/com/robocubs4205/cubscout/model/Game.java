package com.robocubs4205.cubscout.model;

import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import org.springframework.hateoas.Identifiable;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable
public class Game implements Identifiable<Long> {

    @PrimaryKey
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    private Year year;

    @NotPersistent
    private Set<Event> events = new HashSet<>();

    @NotPersistent
    private Scorecard scorecard;

    @NotPersistent
    private Set<Robot> robots = new HashSet<>();

    public Game(){}

    public Game(int id){
        setId(id);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public Scorecard getScorecard() {
        return scorecard;
    }

    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Set<Robot> getRobots() {
        return robots;
    }
}