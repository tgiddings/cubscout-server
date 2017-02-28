package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.Result;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

public class ResultResourceAssembler extends IdentifiableResourceAssemblerSupport<Result,ResultResource> {
    public ResultResourceAssembler() {
        super(RobotController.class, ResultResource.class);
    }

    @Override
    public ResultResource toResource(Result entity) {
         ResultResource resultResource = new ResultResource();
         resultResource.setScore(entity.getScore());
         resultResource.add(linkTo(methodOn(RobotController.class).getResults(entity.getRobot())).withSelfRel());
         resultResource.add(linkTo(methodOn(MatchController.class).getAllResults(entity.getMatch())).withSelfRel());
         return resultResource;
    }
}
