package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.application.dao.GoalDao;
import com.cyrildewit.pgc.application.dao.SqlGoalDao;
import com.cyrildewit.pgc.application.dao.ActivityDao;
import com.cyrildewit.pgc.application.dao.SqlActivityDao;

@Service
public class GoalService implements GoalServiceInterface {
    private final GoalDao goalDao;
    private final ActivityDao activityDao;

    @Autowired
    public GoalService(SqlGoalDao goalDao, SqlActivityDao activityDao) {
        this.goalDao = goalDao;
        this.activityDao = activityDao;
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

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start) {
        return getGoalWithMostRecentActivity(start, LocalDateTime.now());
    }

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start, LocalDateTime end) {
        return goalDao.getGoalWithMostRecentActivity(start, end);
    }

    public Optional<Goal> getGoalWithMostRecentFrequentActivityForUser(User user, int days, LocalDateTime start) {
        return getGoalWithMostRecentFrequentActivityForUser(user, days, start, LocalDateTime.now());
    }

    public Optional<Goal> getGoalWithMostRecentFrequentActivityForUser(User user, int days, LocalDateTime start, LocalDateTime end) {

        Goal goalWithMostRecentFrequentActivity = null;
        long highestFrequency = 0;

        List<Goal> goals = goalDao.selectAllGoalsForUser(user);

        for (Goal goal : goals){
            List<Activity> activities = activityDao.selectActivityWithinPeriodForSubjectAndCauser(goal, user, start, end);
            long currentFrequency = activities.stream().count() / days;

            System.out.println("goal fr: " + currentFrequency);
            System.out.println("user : " + user.getId() + "  goal " + goal.getId());

            if (currentFrequency > highestFrequency) {
                goalWithMostRecentFrequentActivity = goal;
                highestFrequency = currentFrequency;
            }
        }

        return Optional.ofNullable(goalWithMostRecentFrequentActivity);
    }

    public boolean determineIfGoalHastMostRecentFrequentActivityForUser(User user, Goal goal, int days, LocalDateTime start) {
        return determineIfGoalHastMostRecentFrequentActivityForUser(user, goal, days, start, LocalDateTime.now());
    }

    public boolean determineIfGoalHastMostRecentFrequentActivityForUser(User user, Goal goal, int days, LocalDateTime start, LocalDateTime end) {
        Optional<Goal> optionalGoalWithMostRecentFrequentActivity = getGoalWithMostRecentFrequentActivityForUser(user, days, start, end);

        if (optionalGoalWithMostRecentFrequentActivity.isEmpty()) {
            return false;
        }

        return optionalGoalWithMostRecentFrequentActivity.get().getId() == goal.getId();
    }
}
