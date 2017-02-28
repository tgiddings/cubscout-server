package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.Game;
import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

/**
 * Created by trevor on 2/15/17.
 */
public class GameResourceAssembler extends IdentifiableResourceAssemblerSupport<Game,GameResource> {
    public GameResourceAssembler(){
        super(GameController.class,GameResource.class);
    }
    @Override
    public GameResource toResource(Game entity) {
        GameResource resource = createResource(entity);
        resource.setGameId(entity.getId());
        resource.setName(entity.getName());
        resource.setType(entity.getType());
        resource.setYear(entity.getYear());
        return resource;
    }
}
