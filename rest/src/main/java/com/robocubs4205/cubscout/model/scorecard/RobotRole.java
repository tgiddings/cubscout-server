package com.robocubs4205.cubscout.model.scorecard;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@PersistenceCapable
public class RobotRole {

    @PrimaryKey
    private long id;

    @JsonIgnore
    @Persistent
    private Scorecard scorecard;

    @Persistent
    private Set<ScoreWeight> weights = new HashSet<>();

    @NotNull
    private String name;
    private String description;

    public RobotRole() {
    }

    public RobotRole(String name){
        setName(name);
    }

    public Scorecard getScorecard() {
        return scorecard;
    }

    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    @AssertTrue
    public boolean weightsMatchScorecard() {
        return weights.stream().map(ScoreWeight::getField)
                      .map(ScorecardSection::getScorecard)
                      .allMatch(scorecard1 -> Objects.equals(scorecard1.getId(), scorecard.getId()));
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<ScoreWeight> getWeights() {
        return weights;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
