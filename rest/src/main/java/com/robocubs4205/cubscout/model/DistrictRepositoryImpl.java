package com.robocubs4205.cubscout.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.HashSet;
import java.util.Set;

@Repository
public class DistrictRepositoryImpl implements DistrictRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    DistrictRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public District find(String code) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            //in datanucleus, getObjectById returns objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return pm.detachCopy(pm.getObjectById(District.class, code));
        }
    }

    @Override
    public District save(District district) {
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            return pm.makePersistent(district);
        }
    }

    @Override
    public void delete(District district) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            if(JDOHelper.isPersistent(district))pm.deletePersistent(district);
            else pm.newQuery(District.class)
                   .filter("this.id=:id")
                   .deletePersistentAll(district.getId());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<District> findAll() {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            //datanucleus queries return objects in the hollow state when ran outside of
            //transactions. as such, they become transient when the pm closes, even if
            //DetachAllOnCommit is true.
            return new HashSet<>(pm.detachCopyAll(pm.newQuery(District.class).executeList()));
        }
    }
}
