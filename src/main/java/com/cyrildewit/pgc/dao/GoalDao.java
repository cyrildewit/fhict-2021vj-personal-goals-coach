package com.cyrildewit.pgc.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.User;

public interface GoalDao
{
    public List<Goal> selectAllGoals();

    public List<Goal> selectAllGoalsForUser(User user);

    public Optional<Goal> findGoalById(Long id);

    public Optional<Goal> findGoalByUuid(UUID uuid);

    public void insertGoal(Goal goal);

    public boolean updateGoal(Goal goal);

    public void deleteGoalById(Long id);

    public void deleteGoal(Goal goal);

    public Long getTotalGoalsCountForUser(User user);
}