package com.robocubs4205.cubscout.model.scorecard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldSectionRepository extends JpaRepository<FieldSection,Long>{
    FieldSection findById(long id);
    FieldSection findByIdAndScoreCard(long id, Scorecard scorecard);
}
