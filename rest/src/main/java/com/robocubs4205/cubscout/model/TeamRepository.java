package com.robocubs4205.cubscout.model;

import java.util.Set;

public interface TeamRepository{
    Team findById(long id);
    Set<Team> findByNumber(int number);

    Team findByNumberAndGameType(int number, String gameType);

    Set<Team> findAll();

    Team save(Team team);
}
