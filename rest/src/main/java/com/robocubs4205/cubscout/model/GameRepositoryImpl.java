package com.robocubs4205.cubscout.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;

@Repository
public class GameRepositoryImpl implements GameRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    public GameRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Game find(long id) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //in datanucleus, getObjectById returns objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy(pm.getObjectById(Game.class, id));
        }
    }

    @Override
    public Game find(String name) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            Query<Game> q = pm.newQuery(Game.class)
                              .filter("this.name==:name");
            //workaround for bug that will be fixed in datanucleus-5.1.0-m3
            q.setUnique(true);
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy((Game) q.execute(name));
        }
    }

    @Override
    public Set<Game> find(Year year) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Game.class)
                                                    .filter("this.year==year")
                                                    .executeList()));
        }
    }

    @Override
    public Set<Game> findAll() {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Game.class).executeList()));
        }
    }

    @Override
    public Game save(Game game) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.makePersistent(game);
        }
    }

    @Override
    public void delete(Game game) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            if (JDOHelper.isPersistent(game)) pm.deletePersistent(game);
            else pm.newQuery(Game.class).filter("this.id=:id").deletePersistentAll(game.getId());
        }
    }
}
