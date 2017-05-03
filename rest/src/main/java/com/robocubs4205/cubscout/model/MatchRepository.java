package com.robocubs4205.cubscout.model;

import java.util.Set;

public interface MatchRepository{
    Match find(int id);
    Set<Match> find(Event event);
    Match save(Match match);

    Set<Match> findAll();

    void delete(Match match);
}
