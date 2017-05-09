package com.robocubs4205.cubscout.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Repository
@Scope(SCOPE_PROTOTYPE)
public class RobotRepositoryImpl implements RobotRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    public RobotRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Robot find(long id) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.getObjectById(Robot.class, id);
        }
    }

    @Override
    public Set<Robot> find(Team team) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return new HashSet<>(pm.newQuery(Robot.class).filter("this.team==:team").setParameters(team).executeList());
        }
    }

    @Override
    public Robot find(int number, Game game) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            Query<Robot> q = pm.newQuery(Robot.class)
                               .filter("this.number==:number&&this.game==:game")
                               .setParameters(number,game);
            q.setUnique(true);
            return q.executeUnique();
        }
    }

    @Override
    public Set<Robot> findAll() {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return new HashSet<>(pm.newQuery(Robot.class).executeList());
        }
    }

    @Override
    public Robot save(Robot robot) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.makePersistent(robot);
        }
    }

    @Override
    public void delete(Robot robot) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            if(JDOHelper.isPersistent(robot)) pm.deletePersistent(robot);
            else pm.newQuery(Robot.class).filter("this.id==:id").deletePersistentAll(robot.getId());
        }
    }
}