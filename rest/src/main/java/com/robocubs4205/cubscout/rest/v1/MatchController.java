package com.robocubs4205.cubscout.rest.v1;

import com.robocubs4205.cubscout.model.Match;
import com.robocubs4205.cubscout.model.MatchRepository;
import com.robocubs4205.cubscout.model.scorecard.Result;
import com.robocubs4205.cubscout.model.scorecard.ResultRepository;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import com.robocubs4205.cubscout.rest.JsonArrayContainer;
import com.robocubs4205.cubscout.service.ScoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpHeaders.LOCATION;

@RestController
@PreAuthorize("denyAll()")
@RequestMapping(value = "/matches",produces = "application/vnd.robocubs-v1+json")
public class MatchController {
    private final MatchRepository matchRepository;
    private final ResultRepository resultRepository;

    private final ScorecardRepository scorecardRepository;
    private final ScoutService scoutService;

    @Autowired
    public MatchController(MatchRepository matchRepository,
                           ResultRepository resultRepository,
                           ScorecardRepository scorecardRepository,
                           ScoutService scoutService) {
        this.matchRepository = matchRepository;
        this.resultRepository = resultRepository;
        this.scorecardRepository = scorecardRepository;
        this.scoutService = scoutService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public JsonArrayContainer<MatchResource> getAllMatches() {
        return new JsonArrayContainer<>(new MatchResourceAssembler()
                .toResources(matchRepository.findAll()));
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public MatchResource getMatch(@PathVariable Match match) {
        if (match == null)
            throw new ResourceNotFoundException("match does not exist");
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('MANAGE_MATCHES')")
    public MatchResource updateMatch(@PathVariable Match match,
                                     @RequestBody Match newMatch) {
        if (match == null)
            throw new ResourceNotFoundException("match does not exist");
        match.setNumber(newMatch.getNumber());
        matchRepository.save(match);
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('MANAGE_MATCHES')")
    public void deleteMatch(@PathVariable Match match) {
        if (match == null)
            throw new ResourceNotFoundException("match does not exist");
        matchRepository.delete(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}/robots", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public JsonArrayContainer<RobotResource> getAllRobots(@PathVariable Match match) {
        if (match == null)
            throw new ResourceNotFoundException("match does not exist");
        return new JsonArrayContainer<>(new RobotResourceAssembler().toResources(match.getRobots()));
    }

    @RequestMapping(value = "/{match:[0-9]+}/results", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SCOUT_MATCHES')")
    public ResultResource createResult(@PathVariable Match match,
                                       @Validated(Result.Creating.class)
                                       @RequestBody Result result,
                                       HttpServletResponse response) {
        if (match == null) throw new ResourceNotFoundException();

        result.setScorecard(scorecardRepository
                .findById(result.getScorecard().getId()));
        if (result.getScorecard() == null) {
            throw new ScorecardDoesNotExistException();
        }

        ResultResource resultResource = new ResultResourceAssembler().toResource(scoutService.scoutMatch(result,match));

        response.setHeader(LOCATION,resultResource.getLink("self").getHref());
        return resultResource;
    }

    @RequestMapping(value = "/{match:[0-9]+}/results", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public JsonArrayContainer<ResultResource> getAllResults(@PathVariable Match match) {
        if (match == null)
            throw new ResourceNotFoundException("match does not exist");
        return new JsonArrayContainer<>(new ResultResourceAssembler()
                .toResources(resultRepository.findByMatch(match)));
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
            reason = "The Scorecard specified does not exist")
    private class ScorecardDoesNotExistException extends RuntimeException {
    }
}
