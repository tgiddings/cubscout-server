package com.robocubs4205.cubscout.model.scorecard;

import com.robocubs4205.cubscout.model.Game;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class Scorecard implements Identifiable<Long> {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne(optional = false)
    private Game game;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "scorecard")
    @NotEmpty(groups = {Default.class,Creating.class})
    private List<ScorecardSection> sections;

    public Scorecard() {
    }

    public Scorecard(long id){
        setId(id);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<ScorecardSection> getSections() {
        return sections;
    }

    public void setSections(List<ScorecardSection> sections) {
        this.sections = sections;
    }

    public List<FieldSection> getFields() {
        return sections.stream().filter(section -> section instanceof FieldSection)
                       .map(section -> (FieldSection) section).collect(Collectors.toList());
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public interface Creating{

    }
}
