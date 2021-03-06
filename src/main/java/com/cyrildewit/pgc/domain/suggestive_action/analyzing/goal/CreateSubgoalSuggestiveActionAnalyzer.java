package com.cyrildewit.pgc.domain.suggestive_action.analyzing.goal;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.SuggestiveActionAnalyzer;

public class CreateSubgoalSuggestiveActionAnalyzer implements SuggestiveActionAnalyzer {
    private Goal goal;

    public CreateSubgoalSuggestiveActionAnalyzer(Goal goal) {
        this.goal = goal;
    }

    public Optional<SuggestiveAction> analyze() {
        Optional<SuggestiveAction> suggestiveAction = Optional.empty();
        Optional<Activity> optionalLastGoalActivity = goal.getLatestActivity();

        if (goal.getUserId() == 0 ||
                goal.getCoachingStylePreference().isEmpty() ||
                optionalLastGoalActivity.isEmpty()
        ) {
            return suggestiveAction;
        }

        CoachingStylePreference coachingStylePreference = goal.getCoachingStylePreference().get();
        LocalDateTime lastGoalActivityDateTime = optionalLastGoalActivity.get().getCreatedAt();

        if (!coachingStylePreference.isSuggestCreateSubgoalEnabled() ||
                !lastGoalActivityDateTime.isBefore(coachingStylePreference.getSuggestCreateSubgoalAfterLastActivityBeforePeriodDateTime())
        ) {
            return suggestiveAction;
        }

        return Optional.ofNullable(new SuggestiveAction(
                UUID.randomUUID(),
                SuggestiveActionType.CREATE_SUBGOAL,
                goal.getUserId(),
                goal.getId(),
                0
        ));
    }
}