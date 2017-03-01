package com.robocubs4205.cubscout.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.robocubs4205.cubscout.model.*;
import com.robocubs4205.cubscout.model.scorecard.FieldSection;
import com.robocubs4205.cubscout.model.scorecard.FieldSectionRepository;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.data.rest.webmvc.json.patch.Patch;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/matches")
public class MatchController {
    private final MatchRepository matchRepository;
    private final ResultRepository resultRepository;
    private final RobotRepository robotRepository;

    private final RobotController robotController;
    private final ObjectMapper objectMapper;
    private final ScorecardRepository scorecardRepository;
    private final FieldSectionRepository fieldSectionRepository;

    @Autowired
    public MatchController(MatchRepository matchRepository, ResultRepository resultRepository,
                           RobotRepository robotRepository, RobotController robotController,
                           ObjectMapper objectMapper, ScorecardRepository scorecardRepository,
                           FieldSectionRepository fieldSectionRepository) {
        this.matchRepository = matchRepository;
        this.resultRepository = resultRepository;
        this.robotRepository = robotRepository;
        this.robotController = robotController;
        this.objectMapper = objectMapper;
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
/*
    @RequestMapping(value = "/{match:[0-9]+}/robots/{robot:[0-9]+}", method = RequestMethod.GET)
    public RobotResource getRobot(@PathVariable Match match, @PathVariable Robot robot) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        if (robot == null || !robot.getMatches().contains(match)) {
            throw new ResourceNotFoundException("match does not have robot");
        }
        return robotController.get(robot);
    }
*/
    /*@RequestMapping(value = "/{match:[0-9]+}", method = RequestMethod.PATCH)
    public MatchResource patchMatch(@PathVariable Match match, @RequestBody JsonNode patchJson) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        Patch patch = new JsonPatchPatchConverter(objectMapper).convert(patchJson);
        match = patch.apply(match, Match.class);
        for (Result result : match.getResults()) {
            Robot existingRobot = robotRepository.findById(result.getRobot().getId());
            if (existingRobot == null) robotRepository.save(result.getRobot());
            else result.setRobot(existingRobot);
            resultRepository.save(result);
        }
        matchRepository.save(match);
        return new MatchResourceAssembler().toResource(match);
    }*/


    //todo
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
        //validate scores match sections in scorecard
        //todo: reduce database hits
        result.setScores(result.getScores().stream().peek(scorecardFieldResult -> scorecardFieldResult.setField(
                fieldSectionRepository.findByIdAndScoreCard(scorecardFieldResult.getId(), result.getScorecard())))
                               .collect(
                                       Collectors.toList()));
        if (result.getScores().stream().anyMatch(scorecardFieldResult -> scorecardFieldResult.getField() == null)) {
            throw new ScoresDontMatchScorecardException();
        }

        //validate missing scorecard sections correspond only to optional fields
        //todo: move validation logic into domain objects
        if (result.getScorecard().getSections().stream()
                  .filter(scorecardSection -> scorecardSection instanceof FieldSection)
                  .map(scorecardSection -> (FieldSection) scorecardSection)
                  .filter(fieldSection -> !fieldSection.isOptional()).anyMatch(
                        fieldSection -> result.getScores().stream().noneMatch(
                                scorecardFieldResult -> scorecardFieldResult.getField().getId() == fieldSection
                                        .getId()))) {
            throw new RequiredScoresAbsentException();
        }

        //todo: validate null scores correspond only to optional fields

        result.setMatch(match);
        resultRepository.save(result);
    }

    @RequestMapping(value = "/{match:[0-9]+}/results", method = RequestMethod.GET)
    public List<ResultResource> getAllResults(@PathVariable Match match) {
        if (match == null) throw new ResourceNotFoundException("match does not exist");
        return new ResultResourceAssembler().toResources(resultRepository.findByMatch(match));
    }

    /*
        @RequestMapping(value = "/{match}/results", method = RequestMethod.POST)
        public ResultResource createResult(@PathVariable Match match, @RequestBody Result result) {
            if (match == null) throw new ResourceNotFoundException();
            if (match.getAllGames().stream().noneMatch(robot -> robot.getId().equals(result.getGame().getId()))) {
                Robot existingRobot = robotRepository.findById(result.getGame().getId());
                if (existingRobot == null) robotRepository.save(result.getGame());
                else result.setRobot(existingRobot);
                match.getAllGames().add(result.getGame());
            }
            resultRepository.save(result);
            match.getAllGames().add(result);
            matchRepository.save(match);
            return new ResultResourceAssembler().toResource(result);
        }*/
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "The Scorecard specified does not exist")
    class ScorecardDoesNotExistException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "The scores provided do not match the specified" +
            " scorecard")
    class ScoresDontMatchScorecardException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY, reason = "Some scores required by the specified " +
            "scorecard are absent")
    class RequiredScoresAbsentException extends RuntimeException {
    }
}
