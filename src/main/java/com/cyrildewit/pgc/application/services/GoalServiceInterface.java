package com.cyrildewit.pgc.application.services;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.user.model.User;

public interface GoalServiceInterface {
    public List<Goal> getAllGoals();

    public List<Goal> getAllGoalsForUser(User user);

    public Optional<Goal> getGoalById(long id);

    public Optional<Goal> getGoalByUuid(UUID uuid);

    public List<Goal> getGoalsByIds(List<Long> ids);

    public void addGoal(Goal goal);

    public boolean updateGoal(Goal goal);

    public void deleteGoalById(long id);

    public void deleteGoal(Goal goal);

    public long getTotalGoalsCountForUser(User user);

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start);

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start, LocalDateTime end);

    public Optional<Goal> getGoalWithMostRecentFrequentActivityForUser(User user, int days, LocalDateTime start);

    public Optional<Goal> getGoalWithMostRecentFrequentActivityForUser(User user, int days, LocalDateTime start, LocalDateTime end);

    public boolean determineIfGoalHastMostRecentFrequentActivityForUser(User user, Goal goal, int days, LocalDateTime start);

    public boolean determineIfGoalHastMostRecentFrequentActivityForUser(User user, Goal goal, int days, LocalDateTime start, LocalDateTime end);
}