package com.cyrildewit.pgc.services;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.User;

public interface GoalServiceInterface
{
    public List<Goal> getAllGoals();

    public List<Goal> getAllGoalsForUser(User user);

    public Optional<Goal> findGoalById(Integer id);

    public Optional<Goal> findGoalByUuid(UUID uuid);

    public void addGoal(Goal goal);

    public void updateGoal(Goal goal, String[] params);

    public void deleteGoalById(Integer id);

    public void deleteGoal(Goal goal);

    public Long getTotalGoalsCountForUser(User user);
}