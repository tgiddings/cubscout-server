package com.robocubs4205.cubscout.model.scorecard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.HashSet;
import java.util.Set;

@Repository
public class ScorecardRepositoryImpl implements ScorecardRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    ScorecardRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Scorecard find(long id) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            //in datanucleus, getObjectById returns objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy(pm.getObjectById(Scorecard.class,id));
        }
    }

    @Override
    public Set<Scorecard> findAll() {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Scorecard.class).executeList()));
        }
    }

    @Override
    public void delete(Scorecard scorecard) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            if(JDOHelper.isPersistent(scorecard)) pm.deletePersistent(scorecard);
            else pm.newQuery(Scorecard.class).filter("this.id==:id").deletePersistentAll(scorecard);
        }
    }

    @Override
    public Scorecard save(Scorecard scorecard) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return pm.makePersistent(scorecard);
        }
    }
}
