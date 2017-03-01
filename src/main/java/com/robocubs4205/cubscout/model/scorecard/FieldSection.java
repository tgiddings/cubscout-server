package com.robocubs4205.cubscout.model.scorecard;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Created by trevor on 2/27/17.
 */
@Entity
public class FieldSection extends ScorecardSection {
    @Enumerated(EnumType.STRING)
    private FieldType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private FieldSection.NullWhen nullWhen;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String CheckBoxMessage;

    private boolean isOptional;

    public FieldSection() {
    }

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public NullWhen getNullWhen() {
        return nullWhen;
    }

    public void setNullWhen(NullWhen nullWhen) {
        this.nullWhen = nullWhen;
    }

    public String getCheckBoxMessage() {
        return CheckBoxMessage;
    }

    public void setCheckBoxMessage(String checkBoxMessage) {
        CheckBoxMessage = checkBoxMessage;
    }

    public boolean isOptional() {
        return isOptional;
    }

    public void setOptional(boolean optional) {
        isOptional = optional;
    }

    public enum NullWhen {
        CHECKED, UNCHECKED
    }

    public enum FieldType{
        COUNT,RATING
    }
}
