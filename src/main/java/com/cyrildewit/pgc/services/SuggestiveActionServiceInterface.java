package com.cyrildewit.pgc.services;

import java.util.List;

import com.cyrildewit.pgc.logic.model.SuggestiveAction;
import com.cyrildewit.pgc.logic.model.User;
import com.cyrildewit.pgc.logic.model.Goal;

public interface SuggestiveActionServiceInterface
{
    public void addSuggestiveAction(SuggestiveAction suggestiveAction);

    public void addUniqueSuggestiveAction(SuggestiveAction suggestiveAction);

    public boolean suggestiveActionExists(SuggestiveAction suggestiveAction);

    public int getTotalSuggestiveActionsCountForUser(User user);

    public int getTotalSuggestiveActionsCountForGoal(Goal goal);

    public List<SuggestiveAction> getAllSuggestiveActionsForGoal(Goal goal);
}
