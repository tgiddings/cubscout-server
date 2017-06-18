package com.robocubs4205.cubscout.model;

import org.springframework.hateoas.Identifiable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@PersistenceCapable
public class Event implements Identifiable<Long> {

    @PrimaryKey
    private long id;

    @NotNull
    @Persistent
    private Game game;

    @NotNull(groups = {Default.class,Creating.class})
    private String shortName;

    private String address;

    @Persistent
    private District district;

    @Persistent
    private Set<Match> matches = new HashSet<>();

    private LocalDate startDate;

    private LocalDate endDate;

    public Event() {
    }

    public Event(long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Match> getMatches() {
        return matches;
    }

    public interface Creating{}
}
