package com.robocubs4205.cubscout.model.scorecard;

import org.springframework.stereotype.Repository;

@Repository
public interface FieldSectionRepository{
    FieldSection findById(long id);
    FieldSection findByIdAndScorecard(long id, Scorecard scorecard);
}
