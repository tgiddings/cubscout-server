package com.robocubs4205.cubscout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by trevor on 2/15/17.
 */
@RestController
@RequestMapping("/districts")
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
    public DistrictResource create(@RequestBody District district) {
        districtRepository.save(district);
        return new DistrictResourceAssembler().toResource(district);
    }

    @RequestMapping(value = "/{district}", method = RequestMethod.GET)
    public DistrictResource get(@PathVariable District district) {
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
}
