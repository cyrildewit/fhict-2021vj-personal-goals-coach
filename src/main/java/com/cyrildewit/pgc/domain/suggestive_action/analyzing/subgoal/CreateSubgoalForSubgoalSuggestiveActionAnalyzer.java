package com.cyrildewit.pgc.domain.suggestive_action.analyzing.subgoal;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;
import com.cyrildewit.pgc.domain.suggestive_action.analyzing.SuggestiveActionAnalyzer;

public class CreateSubgoalForSubgoalSuggestiveActionAnalyzer implements SuggestiveActionAnalyzer {
    private Subgoal subgoal;

    public CreateSubgoalForSubgoalSuggestiveActionAnalyzer(Subgoal subgoal) {
        this.subgoal = subgoal;
    }

    public Optional<SuggestiveAction> analyze() {
        Optional<SuggestiveAction> suggestiveAction = Optional.empty();

        Optional<Goal> goalOptional = subgoal.getGoal();

        if (goalOptional.isEmpty()) {
            return suggestiveAction;
        }

        Goal goal = goalOptional.get();
        Optional<Activity> lastSubgoalActivityOptional = subgoal.getLatestActivity();

        if (goal.getUserId() == 0 ||
                goal.getCoachingStylePreference().isEmpty() ||
                lastSubgoalActivityOptional.isEmpty()
        ) {
            return suggestiveAction;
        }

        CoachingStylePreference coachingStylePreference = goal.getCoachingStylePreference().get();
        LocalDateTime lastSubgoalActivityDateTime = lastSubgoalActivityOptional.get().getCreatedAt();

        if (!coachingStylePreference.isSuggestCreateSubgoalForSubgoalEnabled() ||
                !lastSubgoalActivityDateTime.isBefore(coachingStylePreference.getSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriodDateTime())) {
            return suggestiveAction;
        }

        return Optional.ofNullable(new SuggestiveAction(
                UUID.randomUUID(),
                SuggestiveActionType.CREATE_SUBGOAL_FOR_SUBGOAL,
                goal.getUserId(),
                goal.getId(),
                subgoal.getId()
        ));
    }
}