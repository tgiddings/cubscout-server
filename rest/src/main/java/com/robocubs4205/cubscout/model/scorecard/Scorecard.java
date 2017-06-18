package com.robocubs4205.cubscout.model.scorecard;

import com.robocubs4205.cubscout.model.Game;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.Identifiable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.validation.groups.Default;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@PersistenceCapable
public class Scorecard implements Identifiable<Long> {

    @PrimaryKey
    private long id;

    @Persistent
    private Game game;

    @NotEmpty(groups = {Default.class,Creating.class})
    @Persistent
    private Set<ScorecardSection> sections = new HashSet<>();

    @Persistent
    private Set<Result> results = new HashSet<>();

    @Persistent
    private Set<RobotRole> robotRoles = new HashSet<>();

    @Persistent
    private RobotRole defaultRole;

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

    public Set<ScorecardSection> getSections() {
        return sections;
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

    public Set<Result> getResults() {
        return results;
    }

    public Set<RobotRole> getRobotRoles() {
        return robotRoles;
    }

    public Set<RobotRole> getRoles() {
        return robotRoles;
    }

    public RobotRole getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole(RobotRole defaultRole) {
        this.defaultRole = defaultRole;
    }

    public interface Creating{

    }
}
