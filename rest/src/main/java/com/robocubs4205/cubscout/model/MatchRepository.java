package com.robocubs4205.cubscout.model;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MatchRepository{
    Match find(int id);
    List<Match> findByEvent(Event event);
    Match save(Match match);

    Set<Match> findAll();

    void delete(Match match);
}
