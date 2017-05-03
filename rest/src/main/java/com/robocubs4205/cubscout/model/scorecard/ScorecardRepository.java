package com.robocubs4205.cubscout.model.scorecard;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ScorecardRepository{
    Scorecard findById(long id);

    Set<Scorecard> findAll();

    void delete(Scorecard scorecard);

    Scorecard save(Scorecard scorecard);
}
