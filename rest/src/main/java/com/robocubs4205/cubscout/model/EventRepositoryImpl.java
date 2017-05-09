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
public class EventRepositoryImpl implements EventRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    EventRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Event find(long id) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return pm.getObjectById(Event.class,id);
        }
    }

    //todo: replace q.setUnique(true)+q.execute with q.executeUnique when datanucleus 5.1.0-m3 is released
    @Override
    public Event find(String shortName) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            Query<Event> q = pm.newQuery(Event.class)
                               .filter("this.shortName==:shortName");
            q.setUnique(true);
            return (Event) q.execute(shortName);
        }
    }

    @Override
    public Set<Event> find(Game game) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            Query<Event> q = pm.newQuery(Event.class)
                               .filter("this.game==:game");
            q.setParameters(game);
            return new HashSet<>(q.executeList());
        }
    }

    @Override
    public Set<Event> findAll() {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return new HashSet<>(pm.newQuery(Event.class).executeList());
        }
    }

    @Override
    public Event save(Event event) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return pm.makePersistent(event);
        }
    }

    @Override
    public void delete(Event event) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            if(JDOHelper.isPersistent(event))pm.deletePersistent(event);
            else pm.newQuery(Event.class).filter("this.id=:id").deletePersistentAll(event.getId());
        }
    }
}