package com.cyrildewit.pgc.application.services;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.goal.model.Goal;

public interface SubgoalServiceInterface
{
    public List<Subgoal> getAllSubgoals();

    public List<Subgoal> getAllSubgoalsForGoal(Goal goal);

    public List<Subgoal> getAllFirstLevelSubgoals(Goal goal);

    public List<Subgoal> getAllSubgoalsForSubgoal(Subgoal subgoal);

    public long getTotalSubgoalsCountForGoal(Goal goal);

    public long getTotalFirstLevelSubgoalsCountForGoal(Goal goal);

    public long getTotalSubgoalsCountForSubgoal(Subgoal subgoal);

    public long getTotalFirstLevelSubgoalsCountForSubgoal(Subgoal subgoal);

    public Optional<Subgoal> getSubgoalById(long id);

    public Optional<Subgoal> getSubgoalByUuid(UUID uuid);

    public void addSubgoal(Subgoal subgoal);

    public void updateSubgoal(Subgoal subgoal);

    public void deleteSubgoalById(long id);

    public void deleteSubgoal(Subgoal subgoal);

    public boolean determineIfSubgoalBelongsToGoal(Subgoal subgoal, Goal goal);

    /**
     * Level must be >= 1.
     *
     * 0 is reserverd for goal.
     */
    public Integer determineSubgoalLevel(Subgoal subgoal);
}