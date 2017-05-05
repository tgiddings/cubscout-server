package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.scorecard.Scorecard;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import com.robocubs4205.cubscout.rest.JsonArrayContainer;
import com.robocubs4205.cubscout.rest.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("denyAll()")
@RequestMapping(value = "/scorecards",produces = "application/vnd.robocubs-v1+json")
public class ScorecardController {

    private final ScorecardRepository scorecardRepository;

    @Autowired
    public ScorecardController(ScorecardRepository scorecardRepository){

        this.scorecardRepository = scorecardRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public JsonArrayContainer<ScorecardResource> getAllScorecards(){
        return new JsonArrayContainer<>(new ScorecardResourceAssembler().toResources(scorecardRepository.findAll()));
    }

    @RequestMapping(value = "/{scorecard:[0-9]+}",method=RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public ScorecardResource getScorecard(@PathVariable Scorecard scorecard){
        if(scorecard==null)throw new ResourceNotFoundException("scorecard does not exist");
        return new ScorecardResourceAssembler().toResource(scorecard);
    }

    @RequestMapping(value = "/{scorecard:[0-9]+}/results",method=RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public JsonArrayContainer<ResultResource> getResults(@PathVariable Scorecard scorecard) {
        if(scorecard==null)throw new ResourceNotFoundException("scorecard does not exist");
        return new JsonArrayContainer<>(new ResultResourceAssembler().toResources(scorecard.getResults()));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/{scorecard:[0-9]+}",method=RequestMethod.DELETE)
    @PreAuthorize("hasRole('MANAGE_SCORECARDS')")
    public void deleteScorecard(@PathVariable Scorecard scorecard){
        if(scorecard==null)throw new ResourceNotFoundException("scorecard does not exist");
        scorecardRepository.delete(scorecard);
    }
}
