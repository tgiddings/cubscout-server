package com.robocubs4205.cubscout.model;

import org.springframework.hateoas.Identifiable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable
public class Robot implements Identifiable<Long> {

    @PrimaryKey
    private long id;

    private Team team;

    @NotNull
    private int number;

    @NotNull
    private int year;

    private String name;
    private Set<Match> matches = new HashSet<>();

    private Game game;

    public Robot(){}

    public Robot(long id){
        setId(id);
    }

    @AssertTrue
    public boolean numberMatchesTeam(){
        return number==team.getNumber();
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

    public Set<Match> getMatches() {
        return matches;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
