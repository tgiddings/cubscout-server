package com.robocubs4205.cubscout.model;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GameRepository{
    Game findById(int id);

    Game findByName(String name);

    List<Game> findByYear(int year);

    Set<Game> findAll();

    Game save(Game game);

    void delete(Game game);
}
