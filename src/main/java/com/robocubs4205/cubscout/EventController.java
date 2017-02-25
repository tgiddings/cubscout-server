package com.robocubs4205.cubscout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/events")
public class EventController {
    private final EventRepository eventRepository;
    private final MatchRepository matchRepository;
    private final MatchController matchController;

    @Autowired
    public EventController(EventRepository eventRepository, MatchRepository matchRepository,
                           MatchController matchController) {
        this.eventRepository = eventRepository;
        this.matchRepository = matchRepository;
        this.matchController = matchController;
    }

    @RequestMapping(value = "/{event}", method = RequestMethod.GET)
    EventResource get(@PathVariable Event event) {
        if (event == null) throw new ResourceNotFoundException();
        return new EventResourceAssembler().toResource(event);
    }


    @RequestMapping(value = "/{event}", method = RequestMethod.PUT)
    EventResource edit(@PathVariable Event event, @RequestBody Event newEvent) {
        if (event == null) throw new ResourceNotFoundException();
        event.setShortName(newEvent.getShortName());
        event.setDistrict(newEvent.getDistrict());
        event.setStartDate(newEvent.getStartDate());
        event.setEndDate(newEvent.getEndDate());
        event.setAddress(newEvent.getAddress());
        eventRepository.save(event);
        return new EventResourceAssembler().toResource(event);
    }

    @RequestMapping(value = "/{event}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Event event) {
        if (event == null) throw new ResourceNotFoundException();
        eventRepository.delete(event);
    }

    @RequestMapping(value = "/{event}/matches", method = RequestMethod.GET)
    List<MatchResource> getMatches(@PathVariable Event event) {
        if (event == null) throw new ResourceNotFoundException();
        return new MatchResourceAssembler().toResources(matchRepository.findByEvent(event));
    }

    @RequestMapping(value = "/{event}/matches/{match}", method = RequestMethod.GET)
    MatchResource getMatch(@PathVariable Event event, @PathVariable Match match) {
        /*if (event == null || match == null) throw new ResourceNotFoundException();
        return new MatchResourceAssembler().toResource(match);*/
        if (event == null) throw new ResourceNotFoundException();
        return matchController.get(match);
    }

    @RequestMapping(value = "/{event}/matches", method = RequestMethod.POST)
    MatchResource createMatch(@PathVariable Event event, @RequestBody Match match) {
        if (event == null) throw new ResourceNotFoundException();
        match.setEvent(event);
        matchRepository.save(match);
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{event}/matches/{match}", method = RequestMethod.PUT)
    MatchResource updateMatch(@PathVariable Event event, @PathVariable Match match, @RequestBody Match newMatch) {
        if (event == null) throw new ResourceNotFoundException();
        return matchController.updateMatch(match,newMatch);
    }

    @RequestMapping(value = "/{event}/matches/{match}",method = RequestMethod.DELETE)
    public void deleteMatch(@PathVariable Event event,@PathVariable Match match){
        if(event==null) throw new ResourceNotFoundException();
        matchController.delete(match);
    }
}
