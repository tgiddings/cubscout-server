package com.robocubs4205.cubscout.model.scorecard;

import com.robocubs4205.cubscout.model.Match;
import com.robocubs4205.cubscout.model.Robot;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"match", "robot"}))
public class Result implements Identifiable<Long> {
    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "match")
    private Match match;

    @ManyToOne(optional = false)
    @JoinColumn(name = "robot")
    @NotNull(groups = {Default.class, Creating.class})
    private Robot robot;

    @ManyToOne(optional = false)
    @NotNull(groups = {Default.class, Creating.class})
    private Scorecard scorecard;

    @OneToMany(mappedBy = "result")
    private Set<ScorecardFieldResult> scores = new HashSet<>();

    public Result() {
    }

    @AssertTrue
    public boolean scoresMatchScorecardSections() {
        return scores.stream().allMatch(
                score -> scorecard.getSections()
                                  .stream()
                                  .filter(section -> section instanceof FieldSection)
                                  .map(section -> (FieldSection) section)
                                  .anyMatch(fieldSection ->
                                          fieldSection.getId() == score
                                                  .getField().getId())
        )||scores.isEmpty();
    }

    @AssertTrue
    public boolean allMissingScoresAreOptional() {
        Stream<FieldSection> requiredSections = scorecard.getSections().stream()
                                                         .filter(scorecardSection -> scorecardSection instanceof FieldSection)
                                                         .map(scorecardSection -> (FieldSection) scorecardSection)
                                                         .filter(fieldSection -> !fieldSection.isOptional());
        //null fields
        return requiredSections.anyMatch(
                fieldSection -> scores.stream()
                                      .filter(scorecardFieldResult -> {
                                          return scorecardFieldResult.getField().getId() ==
                                                  fieldSection.getId();
                                      })
                                      .anyMatch(scorecardFieldResult -> scorecardFieldResult
                                              .getScore() == null)
        ) && requiredSections.noneMatch( //missing fields
                fieldSection -> scores.stream()
                                      .noneMatch(scorecardFieldResult -> {
                                          return scorecardFieldResult.getField()
                                                                     .getId() ==
                                                  fieldSection.getId();
                                      }));
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

    public Set<ScorecardFieldResult> getScores() {
        return scores;
    }

    public Scorecard getScorecard() {
        return scorecard;
    }

    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    public interface Creating {
    }
}
