package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SqlSuggestiveActionDao;
import com.cyrildewit.pgc.application.services.SuggestiveActionServiceInterface;

@Service
public class SuggestiveActionService implements SuggestiveActionServiceInterface
{
    private final SuggestiveActionDao suggestiveActionDao;

    @Autowired
    public SuggestiveActionService(SqlSuggestiveActionDao suggestiveActionDao)
    {
        this.suggestiveActionDao = suggestiveActionDao;
    }

    public void addSuggestiveAction(SuggestiveAction suggestiveAction)
    {
        suggestiveActionDao.insertSuggestiveAction(suggestiveAction);
    }

    public void addUniqueSuggestiveAction(SuggestiveAction suggestiveAction)
    {
        if (suggestiveActionExists(suggestiveAction)) {
            return;
        }

        addSuggestiveAction(suggestiveAction);
    }

    public boolean suggestiveActionExists(SuggestiveAction suggestiveAction) {
        return suggestiveActionDao.suggestiveActionExists(suggestiveAction);
    }

    public int getTotalSuggestiveActionsCountForUser(User user)
    {
        return suggestiveActionDao.getTotalSuggestiveActionsCountForUser(user);
    }

    public int getTotalSuggestiveActionsCountForGoal(Goal goal) {
        return suggestiveActionDao.getTotalSuggestiveActionsCountForGoal(goal);
    }

    public List<SuggestiveAction> getAllSuggestiveActionsForGoal(Goal goal) {
        return suggestiveActionDao.selectAllSuggestiveActionsForGoal(goal);
    }
}
