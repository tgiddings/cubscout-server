package com.robocubs4205.cubscout.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.util.HashSet;
import java.util.List;
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
            return pm.getObjectById(District.class, code);
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
            pm.deletePersistent(district);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<District> findAll() {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return new HashSet<>((List<District>)pm.newQuery(District.class).execute());
        }
    }
}
