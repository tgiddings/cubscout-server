package com.robocubs4205.cubscout.model;

import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.Identifiable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable(detachable = "true")
public class Game implements Identifiable<Long> {

    @PrimaryKey
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @NotNull
    private Year year;

    @Persistent
    private Set<Event> events = new HashSet<>();

    @Persistent
    private Scorecard scorecard;

    @Persistent
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

    @Override
    public boolean equals(Object o){
        if(o==null) return false;
        if(o instanceof Game){
            Game g = (Game)o;
            return new EqualsBuilder().append(id,g.id).append(name,g.name)
                                      .append(type,g.type).append(year,g.year)
                                      .append(events,g.events)
                                      .append(scorecard,g.scorecard)
                                      .append(robots,g.robots).build();
        }
        else return false;
    }

    @Override
    public int hashCode(){
        return new HashCodeBuilder().append(id).append(name).append(type)
                                    .append(year).append(events)               .append(scorecard)
                                    .append(robots).build();
    }
}
