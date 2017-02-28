package com.robocubs4205.cubscout.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.robocubs4205.cubscout.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameRepository gameRepository;
    private final EventRepository eventRepository;
    private final DistrictRepository districtRepository;
    private final EventController eventController;

    @Autowired
    public GameController(GameRepository gameRepository, EventRepository eventRepository,
                          DistrictRepository districtRepository, EventController eventController) {
        this.gameRepository = gameRepository;
        this.eventRepository = eventRepository;
        this.districtRepository = districtRepository;
        this.eventController = eventController;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GameResource> getAllEvents() {
        return new GameResourceAssembler().toResources(gameRepository.findAll());
    }

    @RequestMapping(value = "/{game:[0-9]+}", method = RequestMethod.GET)
    public GameResource getGame(@PathVariable Game game) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        return new GameResourceAssembler().toResource(game);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GameResource createGame(@Valid @RequestBody Game game) {
        gameRepository.save(game);
        return new GameResourceAssembler().toResource(game);
    }

    @RequestMapping(value = "/{game:[0-9]+}", method = RequestMethod.PUT)
    public GameResource updateGame(@PathVariable Game game, @Valid @RequestBody Game newGame) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        game.setName(newGame.getName());
        game.setYear(newGame.getYear());
        game.setType(newGame.getType());
        gameRepository.save(game);
        return new GameResourceAssembler().toResource(game);
    }

    @RequestMapping(value = "/{game:[0-9]+}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGame(@PathVariable Game game) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        gameRepository.delete(game);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events", method = RequestMethod.GET)
    public List<EventResource> getAllEvents(@PathVariable Game game) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        return new EventResourceAssembler().toResources(eventRepository.findByGame(game));
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}", method = RequestMethod.GET)
    public EventResource getEvent(@PathVariable Game game, @PathVariable Event event) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        return eventController.getEvent(event);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public EventResource createEvent(@PathVariable Game game,
                                     @Validated(Event.Creating.class) @RequestBody Event event) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        event.setGame(game);
        event.setDistrict(districtRepository.findByCode(event.getDistrict().getCode()));
        if (event.getDistrict() == null) throw new DistrictDoesNotExistException();
        eventRepository.save(event);
        return new EventResourceAssembler().toResource(event);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}", method = RequestMethod.PUT)
    public EventResource updateEvent(@PathVariable Game game, @PathVariable Event event,
                                     @Validated(Event.Creating.class) @RequestBody Event newEvent) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        return eventController.updateEvent(event, newEvent);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Game game, @PathVariable Event event) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        eventController.deleteEvent(event);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}/matches", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResource createMatch(@PathVariable Game game, @PathVariable Event event,
                                     @Validated(Match.Creating.class) @RequestBody Match match) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || !event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        return eventController.createMatch(event, match);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}/matches", method = RequestMethod.GET)
    public List<MatchResource> getAllMatchesFromEvent(@PathVariable Game game, @PathVariable Event event) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        return eventController.getAllMatches(event);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod.GET)
    public MatchResource getMatch(@PathVariable Game game, @PathVariable Event event, @PathVariable Match match) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        return eventController.getMatch(event, match);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod.PUT)
    public MatchResource updateMatch(@PathVariable Game game, @PathVariable Event event, @PathVariable Match match,
                                     @RequestBody Match newMatch) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        return eventController.updateMatch(event, match, newMatch);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod.PATCH)
    public MatchResource patchMatch(@PathVariable Game game, @PathVariable Event event, @PathVariable Match match,
                                    @RequestBody JsonNode newMatchJson){
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        return eventController.patchMatch(event, match, newMatchJson);
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod
            .DELETE)
    public void deleteMatch(@PathVariable Game game, @PathVariable Event event, @PathVariable Match match) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (event == null || event.getGame().getId().equals(game.getId()))
            throw new ResourceNotFoundException("game does not have that event");
        eventController.deleteMatch(event, match);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "District does not exist")
    class DistrictDoesNotExistException extends RuntimeException {
    }

}
