package com.robocubs4205.cubscout.model.scorecard;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ScorecardFieldResult {

    @PrimaryKey
    private long id;

    @Persistent
    private FieldSection field;

    @JsonIgnore
    private Result result;

    private Float score;

    public ScorecardFieldResult(){}

    public FieldSection getField() {
        return field;
    }

    public void setField(FieldSection field) {
        this.field = field;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
