package com.robocubs4205.cubscout.model.scorecard;

import com.robocubs4205.cubscout.model.Match;
import com.robocubs4205.cubscout.model.Robot;

import java.util.Set;

public interface ResultRepository{
    Set<Result> find(Robot robot);
    Set<Result> find(Match match);
    Result find(Robot robot, Match match);

    void delete(Result result);

    Result save(Result result);
}
