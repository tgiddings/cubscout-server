package com.robocubs4205.cubscout;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


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

    @RequestMapping(value = "/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod.GET)
    MatchResource getMatch(@PathVariable Event event, @PathVariable Match match) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        if (match == null || !Objects.equals(match.getEvent().getId(), event.getId())) {
            throw new ResourceNotFoundException("event does not have that match");
        }
        return matchController.getMatch(match);
    }

    @RequestMapping(value = "/{event:[0-9]+}/matches", method = RequestMethod.POST)
    MatchResource createMatch(@PathVariable Event event, @RequestBody Match match) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        if (match == null || !Objects.equals(match.getEvent().getId(), event.getId())) {
            throw new ResourceNotFoundException("event does not have that match");
        }
        match.setEvent(event);
        matchRepository.save(match);
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod.PUT)
    MatchResource updateMatch(@PathVariable Event event, @PathVariable Match match, @RequestBody Match newMatch) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        if (match == null || !Objects.equals(match.getEvent().getId(), event.getId())) {
            throw new ResourceNotFoundException("event does not have that match");
        }
        return matchController.updateMatch(match, newMatch);
    }

    @RequestMapping(value = "/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod.DELETE)
    public void deleteMatch(@PathVariable Event event, @PathVariable Match match) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        if (match == null || !Objects.equals(match.getEvent().getId(), event.getId())) {
            throw new ResourceNotFoundException("event does not have that match");
        }
        matchController.deleteMatch(match);
    }

    @RequestMapping(value = "/{event:[0-9]+}/matches/{match:[0-9]+}", method = RequestMethod.PATCH)
    public MatchResource patchMatch(@PathVariable Event event, @PathVariable Match match,
                                    @RequestBody JsonNode newMatchJson) {
        if (event == null) throw new ResourceNotFoundException("event does not exist");
        if (match == null || !Objects.equals(match.getEvent().getId(), event.getId())) {
            throw new ResourceNotFoundException("event does not have that match");
        }
        return matchController.patchMatch(match, newMatchJson);
    }
}
