package com.robocubs4205.cubscout.model;

import com.robocubs4205.cubscout.model.scorecard.Result;
import org.springframework.hateoas.Identifiable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable
public class Match implements Identifiable<Long> {

    @PrimaryKey
    private long id;

    @NotNull
    @Persistent
    private Event event;

    @Persistent
    private Set<Robot> robots = new HashSet<>();

    @Persistent
    private Set<Result> results = new HashSet<>();

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

    public Set<Robot> getRobots() {
        return robots;
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

    public Set<Result> getResults() {
        return results;
    }

    public interface Creating{}

}
