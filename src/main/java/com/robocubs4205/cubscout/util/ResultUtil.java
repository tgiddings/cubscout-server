package com.robocubs4205.cubscout.util;

import com.robocubs4205.cubscout.model.scorecard.FieldSection;
import com.robocubs4205.cubscout.model.scorecard.Result;
import com.robocubs4205.cubscout.model.scorecard.ScorecardFieldResult;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ResultUtil {
    public static Collector<Result, ?, Result> resultAverager() {
        class Container {
            private final ConcurrentMap<Long, AtomicInteger> countMap = new ConcurrentHashMap<>();
            private final ConcurrentMap<Long, AtomicReference<Float>> scoreMap = new ConcurrentHashMap<>();
            private final ConcurrentMap<Long, FieldSection> fieldMap = new ConcurrentHashMap<>();
        }
        BiConsumer<Container, Result> accumulator = (container, result) -> {
            //add values from result to existing values in container
            container.scoreMap.forEach((fieldId, score) -> {
                result.getScores().stream()
                      .filter(scorecardFieldResult -> scorecardFieldResult.getField().getId() == fieldId)
                      .peek(scorecardFieldResult -> {
                          container.fieldMap.putIfAbsent(fieldId, scorecardFieldResult.getField());
                      })
                      .filter(scorecardFieldResult -> scorecardFieldResult.getScore() != null)
                      .peek(scorecardFieldResult -> container.countMap.get(fieldId).incrementAndGet())
                      .forEach(scorecardFieldResult -> score.getAndAccumulate(scorecardFieldResult.getScore(),
                                                                              (aFloat, aFloat2) -> aFloat + aFloat2));
            });

            //get fields not in container
            Set<ScorecardFieldResult> missingResults =
                    result.getScores().stream().filter(scorecardFieldResult -> {
                        return container.fieldMap.keySet().stream()
                                                 .noneMatch(id -> scorecardFieldResult.getField().getId() == id);
                    }).collect(Collectors.toSet());

            //add fields to container
            missingResults.stream()
                          .peek(scorecardFieldResult -> container.countMap
                                  .putIfAbsent(scorecardFieldResult.getField().getId(), new AtomicInteger(1)))
                          .peek(scorecardFieldResult -> container.fieldMap
                                  .putIfAbsent(scorecardFieldResult.getField().getId(),
                                               scorecardFieldResult.getField()))
                          .forEach(scorecardFieldResult -> container.scoreMap
                                  .putIfAbsent(scorecardFieldResult.getField().getId(),
                                               new AtomicReference<>(scorecardFieldResult.getScore())));
        };

        return Collector.of(Container::new, accumulator, (container, container2) -> {
            container.countMap.keySet().forEach(key -> container.countMap.get(key).addAndGet(
                    container.countMap.getOrDefault(key, new AtomicInteger(0)).get()));
            container2.countMap.keySet()
                               .forEach(key -> container.countMap.putIfAbsent(key, container2.countMap.get(key)));
            return container;
        }, container -> {
            Result result = new Result();

            container.scoreMap.forEach((fieldId, score) -> {
                ScorecardFieldResult fieldResult = new ScorecardFieldResult();
                fieldResult.setScore(score.get() / container.countMap.keySet().stream()
                                                                     .filter(id -> Objects.equals(id, fieldId))
                                                                     .map(id -> container.countMap.get(id).get())
                                                                     .findFirst().get());
                fieldResult.setField(container.fieldMap.get(fieldId));
                result.getScores().add(fieldResult);
            });

            return result;
        });
    }

    public static Collector<ScorecardFieldResult, ?, Result> resultBuilder() {
        return Collector.of(Result::new,
                            (result, scorecardFieldResult) -> result.getScores().add(scorecardFieldResult),
                            (result, result2) -> {
                                Result result3 = new Result();
                                result3.getScores().addAll(result.getScores());
                                result3.getScores().addAll(result2.getScores());
                                return result3;
                            },
                            Collector.Characteristics.UNORDERED, Collector.Characteristics.IDENTITY_FINISH);
    }
}
