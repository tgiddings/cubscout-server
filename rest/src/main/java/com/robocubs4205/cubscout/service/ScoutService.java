package com.robocubs4205.cubscout.service;

import com.robocubs4205.cubscout.model.*;
import com.robocubs4205.cubscout.model.scorecard.FieldSectionRepository;
import com.robocubs4205.cubscout.model.scorecard.Result;
import com.robocubs4205.cubscout.model.scorecard.ResultRepository;
import com.robocubs4205.cubscout.model.scorecard.ScorecardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

@Service
public class ScoutService {

    private final ScorecardRepository scorecardRepository;
    private final RobotRepository robotRepository;
    private final TeamRepository teamRepository;
    private final FieldSectionRepository fieldSectionRepository;
    private final MatchRepository matchRepository;
    private final ResultRepository resultRepository;

    @Autowired
    public ScoutService(ScorecardRepository scorecardRepository,
                        RobotRepository robotRepository,
                        TeamRepository teamRepository,
                        FieldSectionRepository fieldSectionRepository,
                        MatchRepository matchRepository,
                        ResultRepository resultRepository) {

        this.scorecardRepository = scorecardRepository;
        this.robotRepository = robotRepository;
        this.teamRepository = teamRepository;
        this.fieldSectionRepository = fieldSectionRepository;
        this.matchRepository = matchRepository;
        this.resultRepository = resultRepository;
    }

    @PreAuthorize("hasRole('TEAM_MEMBER')")
    public Result scoutMatch(Result result, Match match) {
        result.setScorecard(scorecardRepository
                                    .findById(result.getScorecard().getId()));
        if (result.getScorecard() == null) {
            throw new ScorecardDoesNotExistException();
        }

        result.setMatch(match);

        //remove null scores
        result.getScores().removeIf(
                fieldResult -> fieldResult.getScore() == null
        );

        //replace transient robot with entity from database
        Robot existingRobot = robotRepository
                .find(result.getRobot().getNumber(), result.getScorecard().getGame());
        if (existingRobot == null) { //create new robot
            //find team for this robot
            Team existingTeam = teamRepository
                    .findByNumberAndGameType(
                            result.getRobot().getNumber(),
                            result.getScorecard().getGame().getType());
            if (existingTeam == null) {
                Team team = new Team();
                team.setNumber(result.getRobot().getNumber());
                team.setGameType(result.getMatch().getEvent().getGame().getType());
                team.setDistrict(result.getMatch().getEvent().getDistrict());
                result.getRobot().setTeam(teamRepository.save(team));
            } else result.getRobot().setTeam(existingTeam);

            result.getRobot().setGame(result.getMatch().getEvent().getGame());

            result.setRobot(robotRepository.save(result.getRobot()));
        } else result.setRobot(existingRobot);

        //replace transient FieldSections with entities from database
        //todo: reduce database hits
        result.getScores().forEach(fieldResult -> {
            fieldResult.setField(fieldSectionRepository.findByIdAndScorecard(fieldResult.getId(),
                                                                             result.getScorecard()));
            if(fieldResult.getField()==null) throw new ScoresDoNotExistException();
            fieldResult.setResult(result);
        });

        if (!result.scoresMatchScorecardSections()) {
            throw new ScoresDoNotMatchScorecardException();
        }

        if (!result.allMissingScoresAreOptional()) {
            throw new RequiredScoresAbsentException();
        }

        if (!result.gameMatchesScorecard()) {
            throw new GameDoesNotMatchScorecardException();
        }

        match.getRobots().add(result.getRobot());
        result.getRobot().getMatches().add(match);
        result.setRobot(robotRepository.save(result.getRobot()));
        matchRepository.save(match);
        return resultRepository.save(result);
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
            reason = "The scores provided do not match any scorecard")
    private class ScoresDoNotExistException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
            reason = "The Scorecard specified does not exist")
    private class ScorecardDoesNotExistException extends RuntimeException {
    }

    //todo: allow detailed errors

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
            reason = "The scores provided do not match the specified" +
                    " scorecard")
    private class ScoresDoNotMatchScorecardException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
            reason = "Some scores required by the specified " +
                    "scorecard are absent or null")
    private class RequiredScoresAbsentException extends RuntimeException {
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY,
            reason = "The Game for this match does not match the " +
                    "game for the specified scorecard")
    private class GameDoesNotMatchScorecardException extends RuntimeException {
    }
}
