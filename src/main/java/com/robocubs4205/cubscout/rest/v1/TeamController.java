package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.RobotRepository;
import com.robocubs4205.cubscout.model.Team;
import com.robocubs4205.cubscout.model.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/teams",produces = "application/vnd.robocubs-v1+json")
public class TeamController {
    private final TeamRepository teamRepository;
    private final RobotRepository robotRepository;

    @Autowired
    public TeamController(TeamRepository teamRepository, RobotRepository robotRepository) {
        this.teamRepository = teamRepository;
        this.robotRepository = robotRepository;
    }
    @RequestMapping(method = RequestMethod.GET)
    List<TeamResource> getAll(){
        return new TeamResourceAssembler().toResources(teamRepository.findAll());
    }
    @RequestMapping(value = "/{team:[0-9]+}",method = RequestMethod.GET)
    TeamResource get(@PathVariable Team team){
        if(team==null) throw new ResourceNotFoundException();
        return new TeamResourceAssembler().toResource(team);
    }
    @RequestMapping(method = RequestMethod.POST)
    public TeamResource create(@Valid @RequestBody Team team){
        team = teamRepository.saveAndFlush(team);
        return new TeamResourceAssembler().toResource(team);
    }
    @RequestMapping(value = "/{team:[0-9]+}",method = RequestMethod.PUT)
    public TeamResource update(@PathVariable Team team, @RequestBody Team newTeam){
        if(team==null) throw new ResourceNotFoundException();
        team.setNumber(newTeam.getNumber());
        team.setName(newTeam.getName());
        team = teamRepository.saveAndFlush(team);
        return new TeamResourceAssembler().toResource(team);
    }
    @RequestMapping(value = "/{team:[0-9]+}/matches",method = RequestMethod.GET)
    public List<RobotResource> getRobots(@PathVariable Team team){
        return new RobotResourceAssembler().toResources(robotRepository.findByTeam(team));
    }
}
