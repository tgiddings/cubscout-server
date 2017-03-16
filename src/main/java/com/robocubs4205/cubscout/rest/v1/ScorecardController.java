package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:8081","http://192.168.254.33:8081","https://api.beta.robocubs4205.com"})
@RequestMapping(value = "/scorecards",produces = "application/vnd.robocubs-v1+json")
public class ScorecardController {

    private final ScorecardRepository scorecardRepository;

    @Autowired
    public ScorecardController(ScorecardRepository scorecardRepository){

        this.scorecardRepository = scorecardRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ScorecardResource> getAllScorecards(){
        return new ScorecardResourceAssembler().toResources(scorecardRepository.findAll());
    }

    @RequestMapping(value = "/{scorecard:[0-9]+}")
    public ScorecardResource getScorecard(@PathVariable Scorecard scorecard){
        if(scorecard==null)throw new ResourceNotFoundException("scorecard does not exist");
        return new ScorecardResourceAssembler().toResource(scorecard);
    }

    @RequestMapping(value = "/{scorecard:[0-9]+}/results")
    public List<ResultResource> getResults(@PathVariable Scorecard scorecard) {
        if(scorecard==null)throw new ResourceNotFoundException("scorecard does not exist");
        return new ResultResourceAssembler().toResources(scorecard.getResults());
    }
}
