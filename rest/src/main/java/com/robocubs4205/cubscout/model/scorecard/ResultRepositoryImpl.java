package com.robocubs4205.cubscout.model.scorecard;

import com.robocubs4205.cubscout.model.Match;
import com.robocubs4205.cubscout.model.Robot;
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
public class ResultRepositoryImpl implements ResultRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    ResultRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Set<Result> find(Robot robot) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return new HashSet<>(pm.newQuery(Result.class).filter("this.robot==:robot").setParameters(robot).executeList());
        }
    }

    @Override
    public Set<Result> find(Match match) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return new HashSet<>(pm.newQuery(Result.class).filter("this.match==:match").setParameters(match).executeList());
        }
    }

    @Override
    public Result find(Robot robot, Match match) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            Query<Result> q = pm.newQuery(Result.class).filter("this.robot==:robot&&this.match==:match").setParameters(robot,match);
            q.setUnique(true);
            return q.executeUnique();
        }
    }

    @Override
    public void delete(Result result) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            if(JDOHelper.isPersistent(result)) pm.deletePersistent(result);
            else pm.newQuery(Result.class).filter("this.id==:id").deletePersistentAll(result.getId());
        }
    }

    @Override
    public Result save(Result result) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.makePersistent(result);
        }
    }
}
