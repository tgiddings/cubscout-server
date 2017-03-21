package com.robocubs4205.cubscout.model.scorecard;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class ScoreWeight {
    @Id
    @GeneratedValue
    private long id;
    @JsonIgnore
    @OneToOne(optional = false)
    private FieldSection field;

    private float weight;

    public ScoreWeight(){}

    public ScoreWeight(float weight){
        setWeight(weight);
    }

    public ScoreWeight(int weight){
        setWeight(weight);
    }

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
}
