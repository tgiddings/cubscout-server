package com.robocubs4205.cubscout.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.HashSet;
import java.util.Set;

@Repository
public class TeamRepositoryImpl implements TeamRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    public TeamRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Team findById(long id) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //in datanucleus, getObjectById returns objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy(pm.getObjectById(Team.class,id));
        }
    }

    @Override
    public Set<Team> findByNumber(int number) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Team.class)
                                   .filter("this.number==:number")
                                   .setParameters(number)
                                   .executeList()));
        }
    }

    @Override
    public Team findByNumberAndGameType(int number, String gameType) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            Query<Team> q = pm.newQuery(Team.class)
                              .filter("this.number==:number&&this.gameType==:gameType")
                              .setParameters(number,gameType);
            //workaround for bug that will be fixed in datanucleus-5.1.0-m3
            q.setUnique(true);
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy(q.executeUnique());
        }
    }

    @Override
    public Set<Team> findAll() {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Team.class).executeList()));
        }
    }

    @Override
    public Team save(Team team) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.makePersistent(team);
        }
    }
}
