package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.Robot;
import com.robocubs4205.cubscout.model.RobotRepository;
import com.robocubs4205.cubscout.model.scorecard.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/robots",produces = "application/vnd.robocubs-v1+json")
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

    @RequestMapping(value = "/{robot:[0-9]+}", method = RequestMethod.GET)
    public RobotResource getRobot(@PathVariable Robot robot) {
        if (robot == null) throw new ResourceNotFoundException();
        return new RobotResourceAssembler().toResource(robot);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public RobotResource create(@RequestBody Robot robot) {
        robotRepository.save(robot);
        return new RobotResourceAssembler().toResource(robot);
    }

    @RequestMapping(value = "/{robot:[0-9]+}", method = RequestMethod.PUT)
    public RobotResource update(@PathVariable Robot robot, @RequestBody Robot newRobot) {
        //robot has no properties currently
        return new RobotResourceAssembler().toResource(robot);
    }

    @RequestMapping(value = "/{robot:[0-9]+}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Robot robot) {
        robotRepository.delete(robot);
    }

    @RequestMapping(value = "/{robot:[0-9]+}/results",method = RequestMethod.GET)
    public List<ResultResource> getResults(@PathVariable Robot robot) {
        if (robot == null) throw new ResourceNotFoundException();
        return new ResultResourceAssembler().toResources(resultRepository.findByRobot(robot));
    }

    @RequestMapping(value = "/{robot:[0-9]+}/matches",method = RequestMethod.GET)
    public List<MatchResource> getMatches(@PathVariable Robot robot) {
        if (robot == null) throw new ResourceNotFoundException();
        return new MatchResourceAssembler().toResources(robot.getMatches());
    }
}
