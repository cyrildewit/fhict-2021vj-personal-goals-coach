package com.cyrildewit.pgc.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.model.SuggestiveAction;

public interface SuggestiveActionDao
{
//    public List<Goal> selectAllGoals();
//
    public List<SuggestiveAction> selectAllSuggestiveActionsForGoal(Goal goal);

//    public Optional<Goal> findGoalById(long id);
//
//    public Optional<Goal> findGoalByUuid(UUID uuid);

    public void insertSuggestiveAction(SuggestiveAction suggestiveAction);

    public boolean suggestiveActionExists(SuggestiveAction suggestiveAction);

//    public void insertGoals(List<Goal> goal);

//    public boolean updateGoal(Goal goal);
//
//    public void deleteGoalById(long id);
//
//    public void deleteGoal(Goal goal);
//
//    public long getTotalGoalsCountForUser(User user);

    public int getTotalSuggestiveActionsCountForUser(User user);

    public int getTotalSuggestiveActionsCountForGoal(Goal goal);
}