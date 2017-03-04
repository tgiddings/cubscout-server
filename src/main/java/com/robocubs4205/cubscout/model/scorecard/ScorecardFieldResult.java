package com.robocubs4205.cubscout.model.scorecard;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ScorecardFieldResult {
    @Id
    private long id;

    @ManyToOne(optional=false)
    private FieldSection field;

    @ManyToOne(optional = false)
    private Result result;

    private Integer score;

    public ScorecardFieldResult(){}

    public FieldSection getField() {
        return field;
    }

    public void setField(FieldSection field) {
        this.field = field;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
