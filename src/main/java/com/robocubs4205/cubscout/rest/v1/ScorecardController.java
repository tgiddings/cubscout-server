package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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
