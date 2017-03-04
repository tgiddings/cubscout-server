package com.robocubs4205.cubscout.rest;

import com.robocubs4205.cubscout.model.Match;
import com.robocubs4205.cubscout.model.MatchRepository;
import com.robocubs4205.cubscout.model.Robot;
import com.robocubs4205.cubscout.model.RobotRepository;
import com.robocubs4205.cubscout.model.scorecard.FieldSectionRepository;
import com.robocubs4205.cubscout.model.scorecard.Result;
import com.robocubs4205.cubscout.model.scorecard.ResultRepository;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchRepository matchRepository;
    private final ResultRepository resultRepository;
    private final RobotRepository robotRepository;

    private final ScorecardRepository scorecardRepository;
    private final FieldSectionRepository fieldSectionRepository;

    @Autowired
    public MatchController(MatchRepository matchRepository, ResultRepository resultRepository,
                           RobotRepository robotRepository,
                           ScorecardRepository scorecardRepository,
                           FieldSectionRepository fieldSectionRepository) {
        this.matchRepository = matchRepository;
        this.resultRepository = resultRepository;
        this.robotRepository = robotRepository;
        this.scorecardRepository = scorecardRepository;
        this.fieldSectionRepository = fieldSectionRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MatchResource> getAllMatches() {
        return new MatchResourceAssembler().toResources(matchRepository.findAll());
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.GET)
    public MatchResource getMatch(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.PUT)
    public MatchResource updateMatch(@PathVariable Match match, @RequestBody Match newMatch) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        match.setNumber(newMatch.getNumber());
        return new MatchResourceAssembler().toResource(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.DELETE)
    public void deleteMatch(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        matchRepository.delete(match);
    }

    @RequestMapping(value = "/{match:[0-9]+}/robots", method = RequestMethod.GET)
    public List<RobotResource> getAllRobots(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        return new RobotResourceAssembler().toResources(match.getRobots());
    }


    @RequestMapping(value = "/{match:[0-9]+}/results", method = RequestMethod.POST)
    public ResultResource createResult(@PathVariable Match match,
                                       @Validated(Result.Creating.class) @RequestBody Result result) {
        if (match == null) throw new ResourceNotFoundException();

        result.setScorecard(scorecardRepository.findById(result.getScorecard().getId()));
        if (result.getScorecard() == null) {
            throw new ScorecardDoesNotExistException();
        }

        Robot existingRobot = robotRepository.findById(result.getRobot().getId());
        if (existingRobot == null) {
            robotRepository.save(result.getRobot()); //create new robot
        } else result.setRobot(existingRobot);

        //remove null scores
        result.getScores().removeIf(scorecardFieldResult -> scorecardFieldResult.getScore() == null);

        //validate scores match sections in scorecard
        if (!result.scoresMatchScorecardSections()) {
            throw new ScoresDontMatchScorecardException();
        }

        //validate missing scorecard sections correspond only to optional fields
        if (result.allMissingScoresAreOptional()) {
            throw new RequiredScoresAbsentException();
        }

        //replace transient ScorecardFields with entities from database
        //todo: reduce database hits
        //noinspection ResultOfMethodCallIgnored
        result.getScores().stream().peek(scorecardFieldResult -> scorecardFieldResult.setField(
                fieldSectionRepository.findByIdAndScorecard(scorecardFieldResult.getId(), result.getScorecard()))).close();

        result.setMatch(match);
        resultRepository.save(result);
        return new ResultResourceAssembler().toResource(result);
    }

    @RequestMapping(value = "/{match:[0-9]+}/results", method = RequestMethod.GET)
    public List<ResultResource> getAllResults(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        return new ResultResourceAssembler().toResources(resultRepository.findByMatch(match));
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "The Scorecard specified does not exist")
    private class ScorecardDoesNotExistException extends RuntimeException {
    }

    //todo: allow detailed errors

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "The scores provided do not match the specified" +
            " scorecard")
    private class ScoresDontMatchScorecardException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Some scores required by the specified " +
            "scorecard are absent or null")
    private class RequiredScoresAbsentException extends RuntimeException {
    }
}
