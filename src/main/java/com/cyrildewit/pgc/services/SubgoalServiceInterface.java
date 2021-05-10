package com.cyrildewit.pgc.services;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.model.Subgoal;
import com.cyrildewit.pgc.model.Goal;

public interface SubgoalServiceInterface
{
    public List<Goal> getAllSubgoals();

    public List<Goal> getAllGoalsForGoal(Goal goal);

    public List<Subgoal> getAllFirstLevelSubgoals(Goal goal);

    public List<Subgoal> getAllSubgoalsForSubgoal(Subgoal subgoal);

    public long getTotalSubgoalsCountForGoal(Goal goal);

    public long getTotalFirstLevelSubgoalsCountForGoal(Goal goal);

    public long getTotalSubgoalsCountForSubgoal(Subgoal subgoal);

    public long getTotalFirstLevelSubgoalsCountForSubgoal(Subgoal subgoal);

    public Optional<Goal> findGoalById(long id);

    public Optional<Goal> findGoalByUuid(UUID uuid);

    public void addGoal(Subgoal subgoal);

    public void updateGoal(Subgoal subgoal);

    public void deleteGoalById(long id);

    public void deleteSubgoal(Subgoal subgoal);

    public boolean determineIfSubgoalBelongsToGoal(Subgoal subgoal, Goal goal);

    /**
     * Level must be >= 1.
     *
     * 0 is reserverd for goal.
     */
    public Integer determineSubgoalLevel();
}