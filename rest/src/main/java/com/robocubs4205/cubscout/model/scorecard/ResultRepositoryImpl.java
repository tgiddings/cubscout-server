package com.robocubs4205.cubscout.model.scorecard;

import com.robocubs4205.cubscout.model.Match;
import com.robocubs4205.cubscout.model.Robot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import java.util.HashSet;
import java.util.Set;

@Repository
public class ResultRepositoryImpl implements ResultRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    ResultRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public Set<Result> find(Robot robot) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Result.class)
                                                    .filter("this.robot==:robot")
                                                    .setParameters(robot)
                                                    .executeList()));
        }
    }

    @Override
    public Set<Result> find(Match match) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(Result.class)
                                                    .filter("this.match==:match")
                                                    .setParameters(match)
                                                    .executeList()));
        }
    }

    @Override
    public Result find(Robot robot, Match match) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            Query<Result> q = pm.newQuery(Result.class)
                                .filter("this.robot==:robot&&this.match==:match")
                                .setParameters(robot, match);
            //workaround for bug that will be fixed in datanucleus-5.1.0-m3
            q.setUnique(true);
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy(q.executeUnique());
        }
    }

    @Override
    public void delete(Result result) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            if (JDOHelper.isPersistent(result)) pm.deletePersistent(result);
            else pm.newQuery(Result.class).filter("this.id==:id")
                   .deletePersistentAll(result.getId());
        }
    }

    @Override
    public Result save(Result result) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.makePersistent(result);
        }
    }
}
