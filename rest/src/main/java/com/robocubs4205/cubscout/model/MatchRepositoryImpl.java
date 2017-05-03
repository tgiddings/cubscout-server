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
public class MatchRepositoryImpl implements MatchRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    public MatchRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Match find(int id) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return pm.getObjectById(Match.class,id);
        }
    }

    @Override
    public Set<Match> find(Event event) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            Query<Match> q = pm.newQuery(Match.class).filter("this.event==:event");
            q.setParameters(event);
            return new HashSet<>(q.executeList());
        }
    }

    @Override
    public Match save(Match match) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return pm.makePersistent(match);
        }
    }

    @Override
    public Set<Match> findAll() {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return new HashSet<>(pm.newQuery(Match.class).executeList());
        }
    }

    @Override
    public void delete(Match match) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            if(JDOHelper.isPersistent(match)) pm.deletePersistent(match);
            else pm.newQuery(Match.class).filter("this.id=:id").deletePersistentAll(match.getId());
        }
    }
}
