package com.robocubs4205.cubscout.model;

import java.util.List;
import java.util.Set;

public interface RobotRepository{
    Robot findById(long id);
    List<Robot> findByTeam(Team team);
    Robot findByNumberAndGame(int number, Game game);

    Set<Robot> findAll();

    Robot save(Robot robot);

    void delete(Robot robot);
}
