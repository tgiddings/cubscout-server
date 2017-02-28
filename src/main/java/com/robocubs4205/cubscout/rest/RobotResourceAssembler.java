package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.Robot;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

/**
 * Created by trevor on 2/18/17.
 */
public class RobotResourceAssembler extends IdentifiableResourceAssemblerSupport<Robot,RobotResource> {
    public RobotResourceAssembler(){
        super(RobotController.class,RobotResource.class);
    }
    @Override
    public RobotResource toResource(Robot entity) {
        RobotResource resource = createResource(entity);
        resource.setRobotId(entity.getId());
        return resource;
    }
}
