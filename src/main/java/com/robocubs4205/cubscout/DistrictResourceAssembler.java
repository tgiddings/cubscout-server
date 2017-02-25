package com.robocubs4205.cubscout;

import org.springframework.hateoas.mvc.IdentifiableResourceAssemblerSupport;

/**
 * Created by trevor on 2/15/17.
 */
public class DistrictResourceAssembler extends IdentifiableResourceAssemblerSupport<District,DistrictResource> {
    public DistrictResourceAssembler(){
        super(DistrictController.class,DistrictResource.class);
    }

    @Override
    public DistrictResource toResource(District entity) {
        DistrictResource resource = createResource(entity);
        resource.setCode(entity.getCode());
        resource.setName(entity.getName());
        return resource;
    }
}
