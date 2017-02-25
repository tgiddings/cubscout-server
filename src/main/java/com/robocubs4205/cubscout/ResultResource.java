package com.robocubs4205.cubscout;

import org.springframework.hateoas.ResourceSupport;

/**
 * Created by trevor on 2/23/17.
 */
public class ResultResource extends ResourceSupport {
    private int score;

    public ResultResource(){}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
