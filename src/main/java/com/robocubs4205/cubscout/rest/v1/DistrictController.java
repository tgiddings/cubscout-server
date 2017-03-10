package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.District;
import com.robocubs4205.cubscout.model.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpHeaders.LOCATION;

@RestController
@RequestMapping(value = "/districts",produces = "application/vnd.robocubs-v1+json")
public class DistrictController {
    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictController(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<DistrictResource> getAll() {
        return new DistrictResourceAssembler().toResources(districtRepository.findAll());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public DistrictResource create(@Valid @RequestBody District district, HttpServletResponse response) {
        if(districtRepository.findByCode(district.getCode())!=null) throw new DistrictConflictException();
        districtRepository.save(district);
        DistrictResource districtResource = new DistrictResourceAssembler().toResource(district);
        response.setHeader(LOCATION,districtResource.getLink("self").getHref());
        return districtResource;
    }

    @RequestMapping(value = "/{district}", method = RequestMethod.GET)
    public DistrictResource getDistrict(@PathVariable District district) {
        return new DistrictResourceAssembler().toResource(district);
    }

    @RequestMapping(value = "/{district}",method = RequestMethod.PUT)
    public DistrictResource edit(@PathVariable District district, @RequestBody District newDistrict) {
        if(district==null)throw new ResourceNotFoundException();
        district.setCode(newDistrict.getCode());
        district.setName(newDistrict.getName());
        districtRepository.save(district);
        return new DistrictResourceAssembler().toResource(district);
    }
    @RequestMapping(value = "/{district}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable District district){
        if(district==null)throw new ResourceNotFoundException();
        districtRepository.delete(district);
    }
    @RequestMapping(value = "/{district}/events",method = RequestMethod.GET)
    public List<EventResource> getEvents(@PathVariable District district) {
        if(district==null)throw new ResourceNotFoundException();
        return new EventResourceAssembler().toResources(district.getEvents());
    }

    @RequestMapping(value = "/{district}/teams",method = RequestMethod.GET)
    public List<TeamResource> getTeams(@PathVariable District district) {
        if(district==null)throw new ResourceNotFoundException();
        return new TeamResourceAssembler().toResources(district.getTeams());
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "District with that code already exists")
    private class DistrictConflictException extends RuntimeException {
    }
}
