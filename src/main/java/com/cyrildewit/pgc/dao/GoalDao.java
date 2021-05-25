package com.cyrildewit.pgc.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.User;

public interface GoalDao
{
    public List<Goal> selectAllGoals();

    public List<Goal> selectAllGoalsForUser(User user);

    public Optional<Goal> findGoalById(long id);

    public Optional<Goal> findGoalByUuid(UUID uuid);

    public void insertGoal(Goal goal);

//    public void insertGoals(List<Goal> goal);

    public boolean updateGoal(Goal goal);

    public void deleteGoalById(long id);

    public void deleteGoal(Goal goal);

    public long getTotalGoalsCountForUser(User user);
}