package com.cyrildewit.pgc.application.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.goal.model.Goal;

public interface SubgoalDao
{
    public List<Subgoal> selectAllSubgoals();

    public List<Subgoal> selectAllSubgoalsForGoal(Goal goal);

    public List<Subgoal> selectAllFirstLevelSubgoalsForGoal(Goal goal);

    public List<Subgoal> selectAllSubgoalsForSubgoal(Subgoal subgoal);

    public long countAllSubgoalsForGoal(Goal goal);

    public long countAllFistLevelSubgoalsForGoal(Goal goal);

    public long countAllSubgoalsForSubgoal(Subgoal subgoal);

    public long countAllFistLevelSubgoalsForSubgoal(Subgoal subgoal);

    public Optional<Subgoal> findSubgoalById(long id);

    public Optional<Subgoal> findSubgoalByUuid(UUID uuid);

    public void insertSubgoal(Subgoal subgoal);

    public boolean updateSubgoal(Subgoal subgoal);

    public void deleteSubgoalById(long id);

    public void deleteSubgoal(Subgoal subgoal);

    /**
     * Level must be >= 1.
     *
     * 0 is reserverd for goal.
     */
    public Integer determineSubgoalLevel(Subgoal subgoal);
}