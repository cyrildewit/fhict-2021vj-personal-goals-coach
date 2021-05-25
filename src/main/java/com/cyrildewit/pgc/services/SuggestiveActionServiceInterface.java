package com.cyrildewit.pgc.services;

import java.util.List;

import com.cyrildewit.pgc.model.SuggestiveAction;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.model.Goal;

public interface SuggestiveActionServiceInterface
{
    public void addSuggestiveAction(SuggestiveAction suggestiveAction);

    public boolean suggestiveActionExists(SuggestiveAction suggestiveAction);

    public int getTotalSuggestiveActionsCountForUser(User user);

    public int getTotalSuggestiveActionsCountForGoal(Goal goal);

    public List<SuggestiveAction> getAllSuggestiveActionsForGoal(Goal goal);
}
