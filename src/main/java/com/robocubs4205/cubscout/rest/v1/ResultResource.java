package com.robocubs4205.cubscout.rest.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robocubs4205.cubscout.model.scorecard.ScorecardFieldResult;
import org.springframework.hateoas.ResourceSupport;

import java.util.Set;

/**
 * Created by trevor on 2/23/17.
 */
public class ResultResource extends ResourceSupport {
    private long id;

    private Set<ScorecardFieldResult> scores;

    public ResultResource(){}

    public Set<ScorecardFieldResult> getScores() {
        return scores;
    }

    public void setScores(Set<ScorecardFieldResult> scores) {
        this.scores = scores;
    }

    @JsonProperty("id")
    public long getResultId() {
        return id;
    }

    public void setResultId(long id) {
        this.id = id;
    }
}
