package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.scorecard.ScorecardFieldResult;
import org.springframework.hateoas.ResourceSupport;

import java.util.Set;

/**
 * Created by trevor on 2/23/17.
 */
public class ResultResource extends ResourceSupport {
    private Set<ScorecardFieldResult> scores;

    public ResultResource(){}

    public Set<ScorecardFieldResult> getScores() {
        return scores;
    }

    public void setScores(Set<ScorecardFieldResult> scores) {
        this.scores = scores;
    }
}
