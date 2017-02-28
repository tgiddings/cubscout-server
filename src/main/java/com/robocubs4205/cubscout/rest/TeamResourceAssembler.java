package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.Team;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by trevor on 2/18/17.
 */
public class TeamResourceAssembler extends IdentifiableResourceAssemblerSupport<Team,TeamResource> {
    public TeamResourceAssembler(){
        super(TeamController.class,TeamResource.class);
    }
    @Override
    public TeamResource toResource(Team entity) {
        TeamResource resource = createResource(entity);
        resource.setName(entity.getName());
        resource.setNumber(entity.getNumber());
        resource.setTeamId(entity.getId());
        resource.add(linkTo(RobotController.class).withRel("robot"));
        return resource;
    }
}
