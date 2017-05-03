package com.robocubs4205.cubscout.model;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TeamRepository{
    Team findById(int id);
    Set<Team> findByNumber(int number);

    Team findByNumberAndGameType(int number, String gameType);

    Set<Team> findAll();

    Team save(Team team);
}
