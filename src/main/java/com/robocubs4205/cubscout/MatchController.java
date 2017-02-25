package com.robocubs4205.cubscout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchRepository matchRepository;
    private final ResultRepository resultRepository;
    private final RobotRepository robotRepository;

    private final RobotController robotController;

    @Autowired
    public MatchController(MatchRepository matchRepository, ResultRepository resultRepository,
                           RobotRepository robotRepository, RobotController robotController) {
        this.matchRepository = matchRepository;
        this.resultRepository = resultRepository;
        this.robotRepository = robotRepository;
        this.robotController = robotController;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MatchResource> getAll() {
        return new MatchResourceAssembler().toResources(matchRepository.findAll());
    }

    @RequestMapping(value = "/{match}", method = RequestMethod.GET)
    public MatchResource get(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException();
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match}", method = RequestMethod.PUT)
    public MatchResource updateMatch(@PathVariable Match match, @RequestBody Match newMatch) {
        if (match == null) throw new ResourceNotFoundException();
        match.setNumber(newMatch.getNumber());
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException();
        matchRepository.delete(match);
    }

    @RequestMapping(value = "/{match}/robots", method = RequestMethod.GET)
    public List<RobotResource> getRobots(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException();
        return new RobotResourceAssembler().toResources(match.getRobots());
    }

    @RequestMapping(value = "/{match}/robots/{robot}", method = RequestMethod.GET)
    public RobotResource getRobot(@PathVariable Match match, @PathVariable Robot robot) {
        if (match == null) throw new ResourceNotFoundException();
        return robotController.get(robot);
    }

    @RequestMapping(value = "/{match}/results", method = RequestMethod.GET)
    public List<ResultResource> getResults(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException();
        return new ResultResourceAssembler().toResources(resultRepository.findByMatch(match));
    }

    @RequestMapping(value = "/{match}/results", method = RequestMethod.POST)
    public ResultResource createResult(@PathVariable Match match, @RequestBody Result result) {
        if (match == null) throw new ResourceNotFoundException();
        if (match.getRobots().stream().noneMatch(robot -> robot.getId().equals(result.getRobot().getId()))) {
            Robot existingRobot = robotRepository.findById(result.getRobot().getId());
            if (existingRobot == null) robotRepository.save(result.getRobot());
            else result.setRobot(existingRobot);
            match.getRobots().add(result.getRobot());
        }
        resultRepository.save(result);
        match.getResults().add(result);
        matchRepository.save(match);
        return new ResultResourceAssembler().toResource(result);
    }
}
