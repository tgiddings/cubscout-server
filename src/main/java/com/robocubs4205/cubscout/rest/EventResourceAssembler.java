package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.Event;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

/**
 * Created by trevor on 2/14/17.
 */
public class EventResourceAssembler extends IdentifiableResourceAssemblerSupport<Event, EventResource> {
    public EventResourceAssembler() {
        super(EventController.class, EventResource.class);
    }

    @Override
    public EventResource toResource(Event entity) {
        EventResource resource = createResource(entity,entity.getGame());
        resource.setShortName(entity.getShortName());
        resource.setEventId(entity.getId());
        resource.setAddress(entity.getAddress());
        resource.setStartDate(entity.getStartDate());
        resource.setEndDate(entity.getEndDate());
        resource.setDistrict(entity.getDistrict().getCode());
        resource.add(linkTo(GameController.class).withRel("game"));
        resource.add(linkTo(DistrictController.class).withRel("district"));
        return resource;
    }
}
