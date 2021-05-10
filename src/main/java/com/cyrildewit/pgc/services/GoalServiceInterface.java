package com.cyrildewit.pgc.services;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.User;

public interface GoalServiceInterface
{
    public List<Goal> getAllGoals();

    public List<Goal> getAllGoalsForUser(User user);

    public Optional<Goal> findGoalById(long id);

    public Optional<Goal> findGoalByUuid(UUID uuid);

    public void addGoal(Goal goal);

    public boolean updateGoal(Goal goal);

    public void deleteGoalById(long id);

    public void deleteGoal(Goal goal);

    public long getTotalGoalsCountForUser(User user);
}