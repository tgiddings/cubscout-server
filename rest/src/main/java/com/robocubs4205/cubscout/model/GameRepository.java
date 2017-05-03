package com.robocubs4205.cubscout.model;

import java.time.Year;
import java.util.Set;

public interface GameRepository{
    Game find(long id);

    Game find(String name);

    Set<Game> find(Year year);

    Set<Game> findAll();

    Game save(Game game);

    void delete(Game game);
}
