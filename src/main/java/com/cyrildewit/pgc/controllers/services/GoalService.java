package com.cyrildewit.pgc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.User;
import com.cyrildewit.pgc.daos.GoalDao;
import com.cyrildewit.pgc.daos.InMemoryGoalDao;

@Service
public class GoalService
{
    private final GoalDao goalDao;

    @Autowired
    public GoalService(InMemoryGoalDao goalDao)
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

    public Optional<Goal> findGoalById(Integer id)
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

    public void deleteGoalById(Integer id)
    {
        goalDao.deleteGoalById(id);
    }

    public void deleteGoal(Goal goal)
    {
        goalDao.deleteGoal(goal);
    }
}
