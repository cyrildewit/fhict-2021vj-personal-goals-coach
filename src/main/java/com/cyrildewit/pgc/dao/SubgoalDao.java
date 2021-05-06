package com.cyrildewit.pgc.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.model.Subgoal;
import com.cyrildewit.pgc.model.Goal;

public interface SubgoalDao
{
    public List<Subgoal> selectAllSubgoals();

    public List<Subgoal> selectAllSubgoalsForGoal(Goal goal);

    public List<Subgoal> selectAllSubgoalsForSubgoal(Subgoal subgoal);

    public Long countAllSubgoalsForGoal(Goal goal);

    public Long countAllSubgoalsForSubgoal(Subgoal subgoal);

    public Optional<Subgoal> findSubgoalById(Long id);

    public Optional<Subgoal> findSubgoalByUuid(UUID uuid);

    public void insertSubgoal(Subgoal subgoal);

    public boolean updateSubgoal(Subgoal subgoal);

    public void deleteSubgoalById(Long id);

    public void deleteSubgoal(Subgoal subgoal);

    /**
     * Level must be >= 1.
     *
     * 0 is reserverd for goal.
     */
    public Integer determineSubgoalLevel(Subgoal subgoal);
}