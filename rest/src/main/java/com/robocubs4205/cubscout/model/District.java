package com.robocubs4205.cubscout.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.hateoas.Identifiable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable(detachable = "true")
public class District implements Identifiable<String> {

    @NotNull
    @PrimaryKey
    private String code;

    @NotNull
    private String name;

    @Persistent
    private Set<Event> events = new HashSet<>();

    @Persistent
    private Set<Team> teams = new HashSet<>();

    public District() {
    }

    public District(String code) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof District) {
            District d = (District) o;
            return new EqualsBuilder().append(code, d.code).append(name, d.name)
                                      .append(events, d.events)
                                      .append(teams, d.teams).build();
        } else return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(code).append(name).append(events)
                                    .append(teams).build();
    }
}
