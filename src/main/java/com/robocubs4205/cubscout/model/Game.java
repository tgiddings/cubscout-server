package com.robocubs4205.cubscout.model;

import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by trevor on 2/14/17.
 */
@Entity
public class Game implements Identifiable<Long> {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    private int year;

    @OneToMany(mappedBy = "game")
    private List<Event> events;

    @OneToOne(mappedBy = "game")
    private Scorecard scorecard;

    @OneToMany(mappedBy = "game")
    private List<Team> teams;

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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Scorecard getScorecard() {
        return scorecard;
    }

    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    void setEvents(List<Event> events){
        this.events = events;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void addEvent(Event event){
        events.add(event);
        event.setGame(this);
    }


}
