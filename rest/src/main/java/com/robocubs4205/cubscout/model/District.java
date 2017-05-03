package com.robocubs4205.cubscout.model;

import org.springframework.hateoas.Identifiable;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class District implements Identifiable<String> {
    @NotNull
    private String code;

    @NotNull
    private String name;

    private Set<Event> events = new HashSet<>();

    private Set<Team> teams = new HashSet<>();

    public District(){}

    public District(String code){
        setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return getCode();
    }

    public Set<Event> getEvents() {
        return events;
    }

    public Set<Team> getTeams() {
        return teams;
    }
}
