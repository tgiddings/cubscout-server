package com.robocubs4205.cubscout.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(BlockJUnit4ClassRunner.class)
public class EventRepositoryImplTests {
    private PersistenceManagerFactory pmf = realPmf();

    private PersistenceManagerFactory realPmf() {
        return JDOHelper.getPersistenceManagerFactory("PU");
    }

    @Before
    public void clearGames() {
        pmf.getPersistenceManager().newQuery(Game.class).deletePersistentAll();
    }

    @Test
    public void findById_NonTX_NoEvents_Throws() {
        EventRepository eventRepository = new EventRepositoryImpl(pmf);
        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(() -> {
            eventRepository.find(1);
        });
    }

    public void findById_NonTX_EventDoesNotExist_Throws() {
        Event event = new Event();
        event.setAddress("foo");
        event.setStartDate(LocalDate.of(1999,12,7));
        event.setEndDate(LocalDate.of(1999,12,10));
        try(PersistenceManager pm = pmf.getPersistenceManager()){
            event = pm.makePersistent(event);
        }
        long id = event.getId();
        EventRepository eventRepository = new EventRepositoryImpl(pmf);

        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(()->{
            eventRepository.find(id+1);
        });
    }
}
