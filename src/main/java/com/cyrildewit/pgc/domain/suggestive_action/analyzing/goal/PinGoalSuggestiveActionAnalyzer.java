package com.cyrildewit.pgc.domain.suggestive_action.analyzing.goal;

import java.util.Optional;
import java.util.UUID;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.SuggestiveActionAnalyzer;

public class PinGoalSuggestiveActionAnalyzer implements SuggestiveActionAnalyzer {
    private Goal goal;

    public PinGoalSuggestiveActionAnalyzer(Goal goal) {
        this.goal = goal;
    }

    public Optional<SuggestiveAction> analyze() {
        Optional<SuggestiveAction> suggestiveAction = Optional.empty();

        if (!goal.hasMostRecentFrequentActivity()) {
            return suggestiveAction;
        }

        return Optional.ofNullable(new SuggestiveAction(
                UUID.randomUUID(),
                SuggestiveActionType.PIN_GOAL,
                goal.getUserId(),
                goal.getId(),
                0
        ));
    }
}