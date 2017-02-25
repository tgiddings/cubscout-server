package com.robocubs4205.cubscout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
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
    public List<GameResource> getAll() {
        return new GameResourceAssembler().toResources(gameRepository.findAll());
    }

    @RequestMapping(value = "/{game}", method = RequestMethod.GET)
    public GameResource get(@PathVariable Game game) {
        if (game == null) throw new ResourceNotFoundException();
        return new GameResourceAssembler().toResource(game);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public GameResource Create(@Valid @RequestBody Game game) {
        gameRepository.save(game);
        return new GameResourceAssembler().toResource(game);
    }

    @RequestMapping(value = "/{game}", method = RequestMethod.PUT)
    public GameResource edit(@PathVariable Game game, @Valid @RequestBody Game newGame) {
        if (game == null) throw new ResourceNotFoundException();
        game.setName(newGame.getName());
        game.setYear(newGame.getYear());
        game.setType(newGame.getType());
        gameRepository.save(game);
        return new GameResourceAssembler().toResource(game);
    }

    @RequestMapping(value = "/{game}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Game game) {
        if (game == null) throw new ResourceNotFoundException();
        gameRepository.delete(game);
    }

    @RequestMapping(value = "/{game}/events", method = RequestMethod.GET)
    public List<EventResource> listEvents(@PathVariable Game game) {
        return new EventResourceAssembler().toResources(eventRepository.findByGame(game));
    }

    @RequestMapping(value = "/{game:[0-9]+}/events/{event:[0-9]+}", method = RequestMethod.GET)
    public EventResource getEvent(@PathVariable Game game, @PathVariable Event event) {
        /*if (game == null || event == null) throw new ResourceNotFoundException();
        return new EventResourceAssembler().toResource(event);*/
        if (game == null) throw new ResourceNotFoundException();
        return eventController.get(event);
    }

    @RequestMapping(value = "/{game}/events", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public EventResource createEvent(@PathVariable Game game, @RequestBody Event event) {
        if (game == null) throw new ResourceNotFoundException();
        event.setGame(game);
        event.setDistrict(districtRepository.findByCode(event.getDistrict().getCode()));
        if (event.getDistrict() == null) throw new ResourceNotFoundException("District does not exist");
        eventRepository.save(event);
        return new EventResourceAssembler().toResource(event);
    }

    @RequestMapping(value = "/{game}/events/{event}", method = RequestMethod.PUT)
    public EventResource editEvent(@PathVariable Game game, @PathVariable Event event, @RequestBody Event newEvent) {
        /*if (game == null || event == null) throw new ResourceNotFoundException();
        event.setShortName(newEvent.getShortName());
        event.setDistrict(newEvent.getDistrict());
        event.setStartDate(newEvent.getStartDate());
        event.setEndDate(newEvent.getEndDate());
        event.setAddress(newEvent.getAddress());
        eventRepository.save(event);
        return new EventResourceAssembler().toResource(event);*/
        if (game == null) throw new ResourceNotFoundException();
        return eventController.edit(event, newEvent);
    }

    @RequestMapping(value = "/{game}/events/{event}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Game game, @PathVariable Event event) {
        /*if (game == null || event == null) throw new ResourceNotFoundException();
        eventRepository.delete(event);*/
        if (game == null) throw new ResourceNotFoundException();
        eventController.delete(event);
    }

    @RequestMapping(value = "/{game}/events/{event}/matches", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public MatchResource createMatch(@PathVariable Game game, @PathVariable Event event, @RequestBody Match match) {
        /*if (game == null || event == null) throw new ResourceNotFoundException();
        match.setEvent(event);
        matchRepository.save(match);
        return new MatchResourceAssembler().toResource(match);*/
        if (game == null) throw new ResourceNotFoundException();
        return eventController.createMatch(event, match);
    }

    @RequestMapping(value = "/{game}/events/{event}/matches")
    public List<MatchResource> getMatches(@PathVariable Game game, @PathVariable Event event){
        if(game==null) throw new ResourceNotFoundException();
        return eventController.getMatches(event);
    }

    @RequestMapping(value = "/{game}/events/{event}/matches/{match}", method = RequestMethod.GET)
    public MatchResource getMatch(@PathVariable Game game, @PathVariable Event event, @PathVariable Match match) {
        /*if (game == null || event == null || match == null) throw new ResourceNotFoundException();
        return new MatchResourceAssembler().toResource(match);*/
        if (game == null) throw new ResourceNotFoundException();
        return eventController.getMatch(event, match);
    }

    @RequestMapping(value = "/{game}/events/{event}/matches/{match}", method = RequestMethod.DELETE)
    public MatchResource updateMatch(@PathVariable Game game, @PathVariable Event event, @PathVariable Match match,
                                     @RequestBody Match newMatch) {
        if (game == null) throw new ResourceNotFoundException();
        return eventController.updateMatch(event,match,newMatch);
    }

    @RequestMapping(value = "/{game}/{event}/matches/{match}",method = RequestMethod.DELETE)
    public void delete(@PathVariable Game game,@PathVariable Event event,@PathVariable Match match){
        if(game==null) throw new ResourceNotFoundException();
        eventController.deleteMatch(event,match);
    }



}
