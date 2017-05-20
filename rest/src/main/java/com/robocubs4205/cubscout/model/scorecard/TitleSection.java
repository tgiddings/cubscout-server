package com.robocubs4205.cubscout.model.scorecard;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class TitleSection extends ScorecardSection {
    private String Title;

    public TitleSection() {
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
