package com.robocubs4205.cubscout.model.scorecard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Repository
@Scope(SCOPE_PROTOTYPE)
public class FieldSectionRepositoryImpl implements FieldSectionRepository {

    private final PersistenceManagerFactory pmf;

    @Autowired
    FieldSectionRepositoryImpl(PersistenceManagerFactory pmf) {
        this.pmf = pmf;
    }

    @Override
    public FieldSection find(long id) {
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            return pm.getObjectById(FieldSection.class,id);
        }
    }

    @Override
    public FieldSection find(long id, Scorecard scorecard) {
        return null;
    }
}
