package com.robocubs4205.cubscout.model;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EventRepository{
    Event find(long id);
    Event find(String shortName);
    Set<Event> find(Game game);
    Set<Event> findAll();
    Event save(Event event);
    void delete(Event event);
}
