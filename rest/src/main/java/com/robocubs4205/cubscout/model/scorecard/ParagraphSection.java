package com.robocubs4205.cubscout.model.scorecard;

import javax.jdo.annotations.PersistenceCapable;

@PersistenceCapable
public class ParagraphSection extends ScorecardSection {
    private String text;

    public ParagraphSection() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
