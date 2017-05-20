package com.robocubs4205.cubscout.model.scorecard;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ScoreWeight {

    @PrimaryKey
    private long id;
    private FieldSection field;

    @JsonIgnore
    private RobotRole robotRole;

    private float weight;

    public ScoreWeight(){}

    public FieldSection getField() {
        return field;
    }

    public void setField(FieldSection field) {
        this.field = field;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RobotRole getRobotRole() {
        return robotRole;
    }

    public void setRobotRole(RobotRole robotRole) {
        this.robotRole = robotRole;
    }
}
