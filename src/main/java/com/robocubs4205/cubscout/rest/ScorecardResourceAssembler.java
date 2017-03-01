package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class ScorecardResourceAssembler extends IdentifiableResourceAssemblerSupport<Scorecard,ScorecardResource>{
    public ScorecardResourceAssembler() {
        super(ScorecardController.class, ScorecardResource.class);
    }

    @Override
    public ScorecardResource toResource(Scorecard entity) {
        ScorecardResource resource = createResource(entity);
        resource.setScorecardId(entity.getId());
        resource.setSections(entity.getSections());
        resource.add(linkTo(GameController.class).slash(entity.getGame()).withRel("game"));
        return resource;
    }
}
