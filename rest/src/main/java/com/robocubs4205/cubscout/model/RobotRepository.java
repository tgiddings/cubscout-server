package com.robocubs4205.cubscout.model;

import java.util.Set;

public interface RobotRepository{
    Robot find(long id);
    Set<Robot> find(Team team);
    Robot find(int number, Game game);

    Set<Robot> findAll();

    Robot save(Robot robot);

    void delete(Robot robot);
}
