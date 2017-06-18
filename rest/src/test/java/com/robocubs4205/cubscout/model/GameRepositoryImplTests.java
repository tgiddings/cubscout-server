package com.robocubs4205.cubscout.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import java.time.Year;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(BlockJUnit4ClassRunner.class)
public class GameRepositoryImplTests {

    private PersistenceManagerFactory pmf = realPmf();

    private PersistenceManagerFactory realPmf() {
        return JDOHelper.getPersistenceManagerFactory("PU");
    }

    @Before
    public void clearGames() {
        pmf.getPersistenceManager().newQuery(Game.class).deletePersistentAll();
    }

    @Test
    public void findById_NonTX_NoGames_Throws() {
        GameRepositoryImpl gameRepository = new GameRepositoryImpl(pmf);
        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(() -> {
            gameRepository.find(1);
        });
    }

    @Test
    public void findById_NonTX_GameDoesNotExist_Throws() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            game = pm.makePersistent(game);
        }
        long id = game.getId();
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(() -> {
            gameRepository.find(id + 1);
        });
    }

    @Test
    public void findById_NonTX_GameExists_returnValueIsDetached() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            game = pm.makePersistent(game);
        }
        long id = game.getId();
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        Game foundGame = gameRepository.find(id);

        assertThat(foundGame).matches(JDOHelper::isDetached);
    }

    @Test
    public void findByName_NonTx_NoGames_Throws() {
        GameRepository gameRepository = new GameRepositoryImpl(pmf);
        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(() -> {
            gameRepository.find("foo");
        });
    }

    @Test
    public void findByName_NonTx_GameDoesNotExist_Throws() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            pm.makePersistent(game);
        }
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(() -> {
            gameRepository.find("bar");
        });
    }

    @Test
    public void findByName_NonTx_GameExists_returnValueIsDetached() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            pm.makePersistent(game);
        }
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        Game foundGame = gameRepository.find("foo");

        assertThat(foundGame).matches(JDOHelper::isDetached);
    }

    @Test
    public void findByYear_NonTx_NoGames_ResultSetIsEmpty() {
        GameRepository gameRepository = new GameRepositoryImpl(pmf);
        Set<Game> foundGames = gameRepository.find(Year.of(1999));
        assertThat(foundGames).isEmpty();
    }

    @Test
    public void findByYear_NonTx_GameDoesNotExist_ResultSetIsEmpty() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            pm.makePersistent(game);
        }
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        Set<Game> foundGames = gameRepository.find(Year.of(1999 + 1));

        assertThat(foundGames).isEmpty();
    }

    @Test
    public void findByYear_NonTx_OneGameExists_returnValueIsDetached() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            pm.makePersistent(game);
        }
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        Set<Game> foundGames = gameRepository.find(Year.of(1999));

        assertThat(foundGames).size().isEqualTo(1);
        assertThat(foundGames).allMatch((JDOHelper::isDetached));
    }

    @Test
    public void findAll_NonTx_NoGames_ResultSetIsEmpty() {
        GameRepository gameRepository = new GameRepositoryImpl(pmf);
        assertThat(gameRepository.findAll()).isEmpty();
    }

    @Test
    public void findAll_NonTx_OneGame_ResultSizeIs1() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            pm.makePersistent(game);
        }
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        Set<Game> foundGames = gameRepository.findAll();

        assertThat(foundGames).size().isEqualTo(1);
    }

    @Test
    public void save_NonTX_ReturnValueIsDetached() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        assertThat(gameRepository.save(game)).matches(JDOHelper::isDetached);
    }

    @Test
    public void save_NonTX_ResultIsInFindAll() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        game = gameRepository.save(game);

        assertThat(gameRepository.findAll()).containsExactly(game);
    }

    @Test
    public void save_NonTX_multipleSaves_ResultsAreInFindAll() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        Game game2 = new Game();
        game2.setName("bar");
        game2.setType("bar");
        game2.setYear(Year.of(1990));
        Game game3 = new Game();
        game3.setName("fooooo");
        game3.setType("bar");
        game3.setYear(Year.of(2999));
        Game game4 = new Game();
        game4.setName("baz");
        game4.setType("bar");
        game4.setYear(Year.of(1991));
        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        game = gameRepository.save(game);
        game2 = gameRepository.save(game2);
        game3 = gameRepository.save(game3);
        game4 = gameRepository.save(game4);

        assertThat(gameRepository.findAll())
            .containsExactlyInAnyOrder(game, game2, game3, game4);
    }

    @Test
    public void delete_NonTX_OneGame_FindAllIsEmpty() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));
        GameRepository gameRepository = new GameRepositoryImpl(pmf);
        game = gameRepository.save(game);

        gameRepository.delete(game);

        assertThat(gameRepository.findAll()).isEmpty();
    }

    @Test
    public void delete_NonTX_OneDeletion_MultipleGames_DeletedNotInFindAll() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));

        Game game2 = new Game();
        game2.setName("bar");
        game2.setType("bar");
        game2.setYear(Year.of(1990));

        Game game3 = new Game();
        game3.setName("fooooo");
        game3.setType("bar");
        game3.setYear(Year.of(2999));

        Game game4 = new Game();
        game4.setName("baz");
        game4.setType("bar");
        game4.setYear(Year.of(1991));

        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        game = gameRepository.save(game);
        game2 = gameRepository.save(game2);
        game3 = gameRepository.save(game3);
        game4 = gameRepository.save(game4);

        gameRepository.delete(game3);

        assertThat(gameRepository.findAll())
            .containsExactlyInAnyOrder(game, game2, game4);
    }

    @Test
    public void delete_NonTX_TwoDeletion_MultipleGames_DeletedNotInFindAll() {
        Game game = new Game();
        game.setName("foo");
        game.setType("bar");
        game.setYear(Year.of(1999));

        Game game2 = new Game();
        game2.setName("bar");
        game2.setType("bar");
        game2.setYear(Year.of(1990));

        Game game3 = new Game();
        game3.setName("fooooo");
        game3.setType("bar");
        game3.setYear(Year.of(2999));

        Game game4 = new Game();
        game4.setName("baz");
        game4.setType("bar");
        game4.setYear(Year.of(1991));

        GameRepository gameRepository = new GameRepositoryImpl(pmf);

        game = gameRepository.save(game);
        game2 = gameRepository.save(game2);
        game3 = gameRepository.save(game3);
        game4 = gameRepository.save(game4);

        gameRepository.delete(game3);
        gameRepository.delete(game2);

        assertThat(gameRepository.findAll())
            .containsExactlyInAnyOrder(game, game4);
    }
}
