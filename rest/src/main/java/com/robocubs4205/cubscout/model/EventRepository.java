package com.robocubs4205.cubscout.model;

import java.util.Set;

public interface EventRepository{
    Event find(long id);
    Event find(String shortName);
    Set<Event> find(Game game);
    Set<Event> findAll();
    Event save(Event event);
    void delete(Event event);
}
