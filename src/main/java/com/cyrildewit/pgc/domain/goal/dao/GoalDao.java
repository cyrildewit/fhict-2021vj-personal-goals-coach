package com.cyrildewit.pgc.domain.goal.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.user.model.User;

public interface GoalDao
{
    public List<Goal> selectAllGoals();

    public List<Goal> selectAllGoalsForUser(User user);

    public Optional<Goal> findGoalById(long id);

    public Optional<Goal> findGoalByUuid(UUID uuid);

    public List<Goal> findGoalByIds(List<Long> ids);

    public void insertGoal(Goal goal);

    public boolean updateGoal(Goal goal);

    public void deleteGoalById(long id);

    public void deleteGoal(Goal goal);

    public long getTotalGoalsCountForUser(User user);

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start, LocalDateTime end);
}