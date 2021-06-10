package com.cyrildewit.pgc.domain.suggestive_action.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;

public interface SuggestiveActionDao
{
    public List<SuggestiveAction> selectAllSuggestiveActionsForGoal(Goal goal);

    public void insertSuggestiveAction(SuggestiveAction suggestiveAction);

    public boolean suggestiveActionExists(SuggestiveAction suggestiveAction);

    public int getTotalSuggestiveActionsCountForUser(User user);

    public int getTotalSuggestiveActionsCountForGoal(Goal goal);
}