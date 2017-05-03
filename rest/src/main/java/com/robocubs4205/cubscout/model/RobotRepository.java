package com.robocubs4205.cubscout.model;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RobotRepository{
    Robot findById(long id);
    List<Robot> findByTeam(Team team);
    Robot findByNumberAndGame(int number, Game game);

    Set<Robot> findAll();

    Robot save(Robot robot);

    void delete(Robot robot);
}
