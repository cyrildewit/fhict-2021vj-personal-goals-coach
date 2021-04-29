package com.cyrildewit.pgc.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.models.Subgoal;
import com.cyrildewit.pgc.models.Goal;

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

    public void updateSubgoal(Subgoal subgoal, String[] params);

    public void deleteSubgoalById(Long id);

    public void deleteSubgoal(Subgoal subgoal);
}