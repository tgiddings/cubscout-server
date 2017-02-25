package com.robocubs4205.cubscout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/robots")
public class RobotController {
    private final RobotRepository robotRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public RobotController(RobotRepository robotRepository, ResultRepository resultRepository) {
        this.robotRepository = robotRepository;
        this.resultRepository = resultRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<RobotResource> getAll() {
        return new RobotResourceAssembler().toResources(robotRepository.findAll());
    }

    @RequestMapping(value = "/{robot}", method = RequestMethod.GET)
    public RobotResource get(@PathVariable Robot robot) {
        if (robot == null) throw new ResourceNotFoundException();
        return new RobotResourceAssembler().toResource(robot);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public RobotResource create(@RequestBody Robot robot) {
        robotRepository.save(robot);
        return new RobotResourceAssembler().toResource(robot);
    }

    @RequestMapping(value = "/{robot}", method = RequestMethod.PUT)
    public RobotResource update(@PathVariable Robot robot, @RequestBody Robot newRobot) {
        //robot has no properties currently
        return new RobotResourceAssembler().toResource(robot);
    }

    @RequestMapping(value = "/{robot}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Robot robot) {
        robotRepository.delete(robot);
    }

    @RequestMapping(value = "/{robot}/results",method = RequestMethod.GET)
    public List<ResultResource> getResults(@PathVariable Robot robot) {
        if (robot == null) throw new ResourceNotFoundException();
        return new ResultResourceAssembler().toResources(resultRepository.findByRobot(robot));
    }

    @RequestMapping(value = "/{robot}/matches",method = RequestMethod.GET)
    public List<MatchResource> getMatches(@PathVariable Robot robot) {
        if (robot == null) throw new ResourceNotFoundException();
        return new MatchResourceAssembler().toResources(robot.getMatches());
    }

    @RequestMapping(value = "/{robot}/matches/{match}/results",method = RequestMethod.GET)
    public ResultResource getResults(@PathVariable Robot robot, @PathVariable Match match) {
        if (robot == null || match == null) throw new ResourceNotFoundException();
        return new ResultResourceAssembler().toResource(resultRepository.findByRobotAndMatch(robot, match));
    }
}
