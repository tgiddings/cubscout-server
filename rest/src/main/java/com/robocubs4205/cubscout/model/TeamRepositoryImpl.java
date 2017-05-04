package com.robocubs4205.cubscout.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Repository
@Scope(SCOPE_PROTOTYPE)
public class TeamRepositoryImpl implements TeamRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    public TeamRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Team findById(long id) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.getObjectById(Team.class,id);
        }
    }

    @Override
    public Set<Team> findByNumber(int number) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return new HashSet<>(pm.newQuery(Team.class)
                                   .filter("this.number==:number")
                                   .setParameters(number)
                                   .executeList());
        }
    }

    @Override
    public Team findByNumberAndGameType(int number, String gameType) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            Query<Team> q = pm.newQuery(Team.class)
                              .filter("this.number==:number&&this.gameType==:gameType")
                              .setParameters(number,gameType);
            q.setUnique(true);
            return q.executeUnique();
        }
    }

    @Override
    public Set<Team> findAll() {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return new HashSet<>(pm.newQuery(Team.class).executeList());
        }
    }

    @Override
    public Team save(Team team) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.makePersistent(team);
        }
    }
}
