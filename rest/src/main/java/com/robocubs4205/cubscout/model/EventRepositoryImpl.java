package com.robocubs4205.cubscout.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.HashSet;
import java.util.Set;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    EventRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Event find(long id) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            //in datanucleus, getObjectById returns objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy(pm.getObjectById(Event.class,id));
        }
    }

    @Override
    public Event find(String shortName) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy((Event) pm.newQuery(Event.class)
                                           .filter("this.shortName==:shortName")
                                           .setParameters(shortName)
                                           .executeUnique());
        }
    }

    @Override
    public Set<Event> findAll() {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Event.class).executeList()));
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
            else pm.newQuery(Event.class).filter("this.id==:id").deletePersistentAll(event.getId());
        }
    }
}
