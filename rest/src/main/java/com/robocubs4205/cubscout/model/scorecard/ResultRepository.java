package com.robocubs4205.cubscout.model.scorecard;

import com.robocubs4205.cubscout.model.Match;
import com.robocubs4205.cubscout.model.Robot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResultRepository{
    List<Result> findByRobot(Robot robot);
    List<Result> findByMatch(Match match);
    Result find(Robot robot, Match match);

    void delete(Result result);

    Result save(Result result);
}
