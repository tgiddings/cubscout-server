package com.robocubs4205.cubscout.model.scorecard;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by trevor on 2/27/17.
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldSection extends ScorecardSection {
    @Enumerated(EnumType.STRING)
    @NotNull
    private FieldType type;

    @NotNull
    private String name;

    private NullWhen nullWhen;

    private String CheckBoxMessage;

    private boolean isOptional;

    @OneToMany(mappedBy = "field")
    private Set<ScorecardFieldResult> results = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<ScorecardFieldResult> getResults() {
        return results;
    }

    public enum NullWhen {
        CHECKED, UNCHECKED
    }

    public enum FieldType{
        COUNT,RATING
    }
}
