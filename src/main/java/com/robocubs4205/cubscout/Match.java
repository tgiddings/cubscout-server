package com.robocubs4205.cubscout;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by trevor on 2/14/17.
 */
@Entity
public class Match implements Identifiable<Long> {
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne
    private Event event;

    @ManyToMany
    private List<Robot> robots;

    @ManyToMany
    private List<Result> results;

    @NotNull(groups = {Default.class,Creating.class})
    private int number;

    @NotNull(groups = {Default.class,Creating.class})
    private String type;

    public Match(){}

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Robot> getRobots() {
        nullRobotsToEmptyList();
        return robots;
    }

    public void setRobots(List<Robot> robots) {
        nullRobotsToEmptyList();
        this.robots = robots;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PreUpdate
    @PrePersist
    void nullRobotsToEmptyList(){
        if(robots==null) robots = new ArrayList<>();
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public interface Creating{}

}
