package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.RobotRepository;
import com.robocubs4205.cubscout.Team;
import com.robocubs4205.cubscout.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/teams")
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
    @RequestMapping(value = "/{team}",method = RequestMethod.GET)
    TeamResource get(@PathVariable Team team){
        if(team==null) throw new ResourceNotFoundException();
        return new TeamResourceAssembler().toResource(team);
    }
    @RequestMapping(method = RequestMethod.POST)
    public TeamResource create(@Valid @RequestBody Team team){
        teamRepository.save(team);
        return new TeamResourceAssembler().toResource(team);
    }
    @RequestMapping(value = "/{team}",method = RequestMethod.PUT)
    public TeamResource update(@PathVariable Team team, @RequestBody Team newTeam){
        if(team==null) throw new ResourceNotFoundException();
        team.setNumber(newTeam.getNumber());
        team.setName(newTeam.getName());
        teamRepository.save(team);
        return new TeamResourceAssembler().toResource(team);
    }
    @RequestMapping(value = "/{team}/matches",method = RequestMethod.GET)
    public List<RobotResource> getRobots(@PathVariable Team team){
        return new RobotResourceAssembler().toResources(robotRepository.findByTeam(team));
    }
}
