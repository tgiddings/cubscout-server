package com.robocubs4205.cubscout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team,Long>{
    Team findById(int id);
    Team findByNumberAndGame(int number, Game game);
}
