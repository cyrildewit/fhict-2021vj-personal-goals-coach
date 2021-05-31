package com.cyrildewit.pgc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.dao.GoalDao;
import com.cyrildewit.pgc.dao.SqlGoalDao;

@Service
public class GoalService {
    private final GoalDao goalDao;

    @Autowired
    public GoalService(SqlGoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public List<Goal> getAllGoals() {
        return goalDao.selectAllGoals();
    }

    public List<Goal> getAllGoalsForUser(User user) {
        return goalDao.selectAllGoalsForUser(user);
    }

    public Optional<Goal> findGoalById(long id) {
        return goalDao.findGoalById(id);
    }

    public Optional<Goal> findGoalByUuid(UUID uuid) {
        return goalDao.findGoalByUuid(uuid);
    }

    public List<Goal> findGoalByIds(List<Long> ids) {
        return goalDao.findGoalByIds(ids);
    }

    public void addGoal(Goal goal) {
        goalDao.insertGoal(goal);
    }

    public boolean updateGoal(Goal goal) {
        return goalDao.updateGoal(goal);
    }

    public void deleteGoalById(long id) {
        goalDao.deleteGoalById(id);
    }

    public void deleteGoal(Goal goal) {
        goalDao.deleteGoal(goal);
    }

    public long getTotalGoalsCountForUser(User user) {
        return goalDao.getTotalGoalsCountForUser(user);
    }

    public Optional<Goal> getGoalWithMostRecentActivity() {
        return goalDao.getGoalWithMostRecentActivity();
    }
}
