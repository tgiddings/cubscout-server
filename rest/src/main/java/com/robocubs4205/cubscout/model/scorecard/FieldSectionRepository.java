package com.robocubs4205.cubscout.model.scorecard;

import org.springframework.stereotype.Repository;

@Repository
public interface FieldSectionRepository{
    FieldSection find(long id);
    FieldSection find(long id, Scorecard scorecard);
}
