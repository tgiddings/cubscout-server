package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.scorecard.ScorecardFieldResult;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Created by trevor on 2/23/17.
 */
public class ResultResource extends ResourceSupport {
    private List<ScorecardFieldResult> scores;

    public ResultResource(){}

    public List<ScorecardFieldResult> getScores() {
        return scores;
    }

    public void setScores(List<ScorecardFieldResult> scores) {
        this.scores = scores;
    }
}
