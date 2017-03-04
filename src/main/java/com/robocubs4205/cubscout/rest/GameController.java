package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.*;
import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import com.robocubs4205.cubscout.model.scorecard.ScorecardSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {
    private final GameRepository gameRepository;
    private final EventRepository eventRepository;
    private final DistrictRepository districtRepository;
    private final ScorecardRepository scorecardRepository;
    private final ScorecardSectionRepository scorecardSectionRepository;

    @Autowired
    public GameController(GameRepository gameRepository, EventRepository eventRepository,
                          DistrictRepository districtRepository,
                          ScorecardRepository scorecardRepository,
                          ScorecardSectionRepository scorecardSectionRepository) {
        this.gameRepository = gameRepository;
        this.eventRepository = eventRepository;
        this.districtRepository = districtRepository;
        this.scorecardRepository = scorecardRepository;
        this.scorecardSectionRepository = scorecardSectionRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<GameResource> getAllGames() {
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

    @RequestMapping(value = "/{game:[0-9]+}/events", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public EventResource createEvent(@PathVariable Game game,
                                     @Validated(Event.Creating.class) @RequestBody Event event) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        event.setGame(game);
        if(event.getDistrict()!=null) {
            event.setDistrict(districtRepository.findByCode(event.getDistrict().getCode()));
            if (event.getDistrict() == null) throw new DistrictDoesNotExistException();
        }
        eventRepository.save(event);
        return new EventResourceAssembler().toResource(event);
    }

    @RequestMapping(value = "/{game:[0-9]+}/scorecards",method = RequestMethod.GET)
    public List<ScorecardResource> getAllScorecards(@PathVariable Game game) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if(game.getScorecard()==null) return new ArrayList<>();
        else return new ScorecardResourceAssembler().toResources(Collections.singletonList(game.getScorecard()));
    }


    @RequestMapping(value = "/{game:[0-9]+}/scorecards", method = RequestMethod.POST)
    public ScorecardResource createScorecard(@PathVariable Game game,
                                             @Validated(Scorecard.Creating.class) @RequestBody Scorecard scorecard) {
        if (game == null) throw new ResourceNotFoundException("game does not exist");
        if (game.getScorecard() != null) throw new GameAlreadyHasScorecardException();
        scorecard.setGame(game);
        scorecardRepository.save(scorecard);
        scorecard.getSections().forEach(scorecardSection->scorecardSection.setScorecard(scorecard));
        scorecardSectionRepository.save(scorecard.getSections());
        game.setScorecard(scorecard);
        gameRepository.save(game);
        return new ScorecardResourceAssembler().toResource(scorecard);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "District does not exist")
    class DistrictDoesNotExistException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "game already has a scorecard. currently, only one " +
            "scorecard per game is supported")
    class GameAlreadyHasScorecardException extends RuntimeException {
    }
}
