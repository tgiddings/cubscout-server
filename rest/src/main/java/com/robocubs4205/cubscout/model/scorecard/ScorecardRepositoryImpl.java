package com.robocubs4205.cubscout.model.scorecard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Repository
@Scope(SCOPE_PROTOTYPE)
public class ScorecardRepositoryImpl implements ScorecardRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    ScorecardRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Scorecard find(long id) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return pm.getObjectById(Scorecard.class,id);
        }
    }

    @Override
    public Set<Scorecard> findAll() {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return new HashSet<>(pm.newQuery(Scorecard.class).executeList());
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
