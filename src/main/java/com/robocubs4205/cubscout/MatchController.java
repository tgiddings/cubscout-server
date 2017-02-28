package com.robocubs4205.cubscout;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.data.rest.webmvc.json.patch.Patch;
import org.springframework.data.rest.webmvc.json.patch.PatchConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchRepository matchRepository;
    private final ResultRepository resultRepository;
    private final RobotRepository robotRepository;

    private final RobotController robotController;
    private final ObjectMapper objectMapper;

    @Autowired
    public MatchController(MatchRepository matchRepository, ResultRepository resultRepository,
                           RobotRepository robotRepository, RobotController robotController,
                           ObjectMapper objectMapper) {
        this.matchRepository = matchRepository;
        this.resultRepository = resultRepository;
        this.robotRepository = robotRepository;
        this.robotController = robotController;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MatchResource> getAllMatches() {
        return new MatchResourceAssembler().toResources(matchRepository.findAll());
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.GET)
    public MatchResource getMatch(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.PUT)
    public MatchResource updateMatch(@PathVariable Match match, @RequestBody Match newMatch) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        match.setNumber(newMatch.getNumber());
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.DELETE)
    public void deleteMatch(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        matchRepository.delete(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}/robots", method = RequestMethod.GET)
    public List<RobotResource> getAllRobots(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        return new RobotResourceAssembler().toResources(match.getRobots());
    }

    @RequestMapping(value = "/{match:[0-9]+}/robots/{robot:[0-9]+}", method = RequestMethod.GET)
    public RobotResource getRobot(@PathVariable Match match, @PathVariable Robot robot) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        if (robot == null || !robot.getMatches().contains(match)) {
            throw new ResourceNotFoundException("match does not have robot");
        }
        return robotController.get(robot);
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.PATCH)
    public MatchResource patchMatch(@PathVariable Match match, @RequestBody JsonNode patchJson) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        Patch patch = new JsonPatchPatchConverter(objectMapper).convert(patchJson);
        match = patch.apply(match, Match.class);
        for (Result result : match.getResults()) {
            Robot existingRobot = robotRepository.findById(result.getRobot().getId());
            if (existingRobot == null) robotRepository.save(result.getRobot());
            else result.setRobot(existingRobot);
            resultRepository.save(result);
        }
        matchRepository.save(match);
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}/results", method = RequestMethod.GET)
    public List<ResultResource> getAllResults(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        return new ResultResourceAssembler().toResources(resultRepository.findByMatch(match));
    }

/*
    @RequestMapping(value = "/{match}/results", method = RequestMethod.POST)
    public ResultResource createResult(@PathVariable Match match, @RequestBody Result result) {
        if (match == null) throw new ResourceNotFoundException();
        if (match.getAllEvents().stream().noneMatch(robot -> robot.getId().equals(result.getGame().getId()))) {
            Robot existingRobot = robotRepository.findById(result.getGame().getId());
            if (existingRobot == null) robotRepository.save(result.getGame());
            else result.setRobot(existingRobot);
            match.getAllEvents().add(result.getGame());
        }
        resultRepository.save(result);
        match.getAllEvents().add(result);
        matchRepository.save(match);
        return new ResultResourceAssembler().toResource(result);
    }*/
}
