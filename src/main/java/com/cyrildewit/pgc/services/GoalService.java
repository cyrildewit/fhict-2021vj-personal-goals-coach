package com.cyrildewit.pgc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.User;
import com.cyrildewit.pgc.dao.GoalDao;
import com.cyrildewit.pgc.dao.SqlGoalDao;

@Service
public class GoalService
{
    private final GoalDao goalDao;

    @Autowired
    public GoalService(SqlGoalDao goalDao)
    {
        this.goalDao = goalDao;
    }

    public List<Goal> getAllGoals()
    {
        return goalDao.selectAllGoals();
    }

    public List<Goal> getAllGoalsForUser(User user)
    {
        return goalDao.selectAllGoalsForUser(user);
    }

    public Optional<Goal> findGoalById(Long id)
    {
        return goalDao.findGoalById(id);
    }

    public Optional<Goal> findGoalByUuid(UUID uuid)
    {
        return goalDao.findGoalByUuid(uuid);
    }

    public void addGoal(Goal goal)
    {
        goalDao.insertGoal(goal);
    }

    public void updateGoal(Goal goal, String[] params)
    {
        goalDao.insertGoal(goal);
    }

    public void deleteGoalById(Long id)
    {
        goalDao.deleteGoalById(id);
    }

    public void deleteGoal(Goal goal)
    {
        goalDao.deleteGoal(goal);
    }

    public Long getTotalGoalsCountForUser(User user)
    {
        return goalDao.getTotalGoalsCountForUser(user);
    }
}
