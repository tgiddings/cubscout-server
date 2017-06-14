package com.robocubs4205.cubscout.model;


import org.datanucleus.api.jdo.JDOPersistenceManagerFactory;
import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.Assert;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.time.Year;

@RunWith(MockitoJUnitRunner.class)
public class GameRepositoryImplTests {

    @Spy
    PersistenceManagerFactory pmf = realPmf();

    private PersistenceManagerFactory realPmf() {
        return JDOHelper.getPersistenceManagerFactory("unit-test-pu");
    }

    @After
    public void clearGames(){
        pmf.getPersistenceManager().newQuery(Game.class).deletePersistentAll();
    }

    @Test(expected = JDOObjectNotFoundException.class)
    public void findById_NonTX_NoGames_Throws(){
        GameRepositoryImpl gameRepository = new GameRepositoryImpl(pmf);
        gameRepository.find(1);
    }
    @Test(expected = JDOObjectNotFoundException.class)
    public void findById_NonTX_GameDoesNotExist_Throws(){
        PersistenceManager pm = pmf.getPersistenceManager();
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        pm.makePersistent(game);
        long id = game.getId();
        pm.close();

        GameRepositoryImpl gameRepository = new GameRepositoryImpl(pmf);
        gameRepository.find(id+1);
    }

    @Test
    public void findById_NonTX_GameExists_returnValueIsDetached(){
        PersistenceManager pm = pmf.getPersistenceManager();
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        //InsertRequest constructor
        pm.makePersistent(game);
        long id = game.getId();
        pm.close();

        GameRepositoryImpl gameRepository = new GameRepositoryImpl(pmf);
        Game foundGame = gameRepository.find(id);
        Assert.isTrue(JDOHelper.isDetached(foundGame),"game returned by findById must be detached");
    }
}
