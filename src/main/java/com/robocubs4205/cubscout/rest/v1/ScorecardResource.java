package com.robocubs4205.cubscout.rest.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.robocubs4205.cubscout.model.scorecard.ScorecardSection;
import org.springframework.hateoas.ResourceSupport;

import java.util.List;

/**
 * Created by trevor on 2/28/17.
 */
public class ScorecardResource extends ResourceSupport{
    private long id;

    private List<ScorecardSection> sections;

    public ScorecardResource(){}

    public List<ScorecardSection> getSections() {
        return sections;
    }

    public void setSections(List<ScorecardSection> sections) {
        this.sections = sections;
    }

    @JsonProperty("id")
    public long getScorecardId() {
        return id;
    }

    public void setScorecardId(long id) {
        this.id = id;
    }
}
