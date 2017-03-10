package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.*;
import com.robocubs4205.cubscout.model.scorecard.Result;
import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import com.robocubs4205.cubscout.model.scorecard.ScorecardFieldResult;
import com.robocubs4205.cubscout.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.FieldResult;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/events", produces = "application/vnd.robocubs-v1+json")
public class EventController {
    private final EventRepository eventRepository;
    private final MatchRepository matchRepository;

    @Autowired
    public EventController(EventRepository eventRepository, MatchRepository matchRepository) {
        this.eventRepository = eventRepository;
        this.matchRepository = matchRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    List<EventResource> getAllEvents() {
        return new EventResourceAssembler().toResources(eventRepository.findAll());
    }

    @RequestMapping(value = "/{event:[0-9]+}", method = RequestMethod.GET)
    EventResource getEvent(@PathVariable Event event) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        return new EventResourceAssembler().toResource(event);
    }


    @RequestMapping(value = "/{event:[0-9]+}", method = RequestMethod.PUT)
    EventResource updateEvent(@PathVariable Event event, @RequestBody Event newEvent) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        event.setShortName(newEvent.getShortName());
        event.setDistrict(newEvent.getDistrict());
        event.setStartDate(newEvent.getStartDate());
        event.setEndDate(newEvent.getEndDate());
        event.setAddress(newEvent.getAddress());
        eventRepository.save(event);
        return new EventResourceAssembler().toResource(event);
    }

    @RequestMapping(value = "/{event:[0-9]+}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteEvent(@PathVariable Event event) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        eventRepository.delete(event);
    }

    @RequestMapping(value = "/{event:[0-9]+}/matches", method = RequestMethod.GET)
    List<MatchResource> getAllMatches(@PathVariable Event event) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        return new MatchResourceAssembler().toResources(matchRepository.findByEvent(event));
    }

    @RequestMapping(value = "/{event:[0-9]+}/matches", method = RequestMethod.POST)
    MatchResource createMatch(@PathVariable Event event, @RequestBody Match match) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        match.setEvent(event);
        matchRepository.save(match);
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{event:[0-9]+}/results", method = RequestMethod.GET)
    List<ResultResource> getResults(@PathVariable Event event, @RequestParam("scorecard") Scorecard scorecard) {
        Function<Match, Set<Result>> resultsForScorecard = match -> match.getResults().stream().filter(result -> {
            return Objects
                    .equals(result.getScorecard().getId(), scorecard.getId());
        }).collect(Collectors.toSet());

        BiFunction<Set<Result>, Robot, Set<Result>> resultsForRobot = (results, robot) -> {
            return results.stream().filter(result -> Objects.equals(result.getRobot().getId(), robot.getId()))
                          .collect(Collectors.toSet());
        };

        Function<Match, Set<Result>> averageResultsForMatch =
                match -> match.getRobots().stream()
                              .map(robot -> resultsForRobot.apply(resultsForScorecard.apply(match), robot)
                                                           .stream().collect(ResultUtil.resultAverager()))
                              .peek(result -> result.setMatch(match))
                              .collect(Collectors.toSet());

        BiFunction<Event,Set<Result>,Set<Result>> averageResultsForEventFromAverageResultsForMatch =
                (event1,results) -> event.getMatches().stream()
                                         .flatMap(match -> match.getRobots().stream())
                                         .map(robot -> resultsForRobot.apply(results,robot).stream()
                                                                      .collect(ResultUtil.resultAverager()))
                                         .collect(Collectors.toSet());

        Set<Result> results =
        event.getMatches().stream().map(averageResultsForMatch).flatMap(Collection::stream).collect(Collectors.toSet());

        return new ResultResourceAssembler().toResources(results);
    }
}
