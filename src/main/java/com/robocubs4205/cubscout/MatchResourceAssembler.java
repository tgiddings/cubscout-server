package com.robocubs4205.cubscout;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class MatchResourceAssembler extends IdentifiableResourceAssemblerSupport<Match,MatchResource> {

    public MatchResourceAssembler() {
        super(MatchController.class, MatchResource.class);
    }

    @Override
    public MatchResource toResource(Match entity) {
        MatchResource resource = createResource(entity);
        resource.setMatchId(entity.getId());
        resource.setNumber(entity.getNumber());
        resource.setType(entity.getType());
        resource.add(linkTo(EventController.class).withRel("event"));
        return resource;
    }
}
