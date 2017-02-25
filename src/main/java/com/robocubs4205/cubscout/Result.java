package com.robocubs4205.cubscout;

import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"match","robot"}))
public class Result implements Identifiable<Long>{
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne
    @NotNull
    @JoinColumn(name="match")
    private Match match;
    @ManyToOne
    @NotNull
    @JoinColumn(name="robot")
    private Robot robot;

    //todo: detailed scores
    @NotNull
    private int score;

    public Result() {
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
