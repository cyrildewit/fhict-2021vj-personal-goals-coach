package com.cyrildewit.pgc.process;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.Subgoal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.services.UserService;
import com.cyrildewit.pgc.services.GoalService;
import com.cyrildewit.pgc.services.SubgoalService;
import com.cyrildewit.pgc.services.SuggestiveActionService;
import com.cyrildewit.pgc.process.ProcessInterface;
import com.cyrildewit.pgc.model.SuggestiveAction;
import com.cyrildewit.pgc.enums.SuggestiveActionType;

public class SuggestiveActionAnalyzerProcess implements ProcessInterface {
    private GoalService goalService;
    private SubgoalService subgoalService;
    private SuggestiveActionService suggestiveActionService;

    private User user;

    private List<SuggestiveAction> suggestiveActions = new ArrayList<SuggestiveAction>();

    @Autowired
    public SuggestiveActionAnalyzerProcess(User user, GoalService goalService, SubgoalService subgoalService, SuggestiveActionService suggestiveActionService) {
        this.user = user;
        this.goalService = goalService;
        this.subgoalService = subgoalService;
        this.suggestiveActionService = suggestiveActionService;
    }

    public void execute() {
        analayzeData();
        storeSuggestiveActions();
    }

    private void analayzeData() {
        getGoals()
                .stream()
                .forEach(goal -> {

                    analyzeGoal(goal);

                    getSubgoalsFromGoal(goal)
                            .stream()
                            .forEach(subgoal -> {
                                analyzeSubgoal(subgoal);
                            });
                });
    }

    private void storeSuggestiveActions() {
        suggestiveActions.stream()
                .forEach(suggestiveAction -> {
                    System.out.println("Suggestive action bestaat: "  + suggestiveActionService.suggestiveActionExists(suggestiveAction));
                    if (suggestiveActionService.suggestiveActionExists(suggestiveAction)) {
                        return;
                    }

                    suggestiveActionService.addSuggestiveAction(suggestiveAction);
                });
    }

    private List<Goal> getGoals() {
        return goalService.getAllGoalsForUser(user);
    }

    private List<Subgoal> getSubgoalsFromGoal(Goal goal) {
        return subgoalService.getAllSubgoalsForGoal(goal);
    }

    private void analyzeGoal(Goal goal) {
        LocalDateTime lastGoalActivityDateTime = LocalDateTime.now().minusWeeks(3);

        if (lastGoalActivityDateTime.isBefore(LocalDateTime.now().minusMonths(3))) {
            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.DELETE_GOAL, user.getId(), goal.getId(), 0));
        } else if (lastGoalActivityDateTime.isBefore(LocalDateTime.now().minusWeeks(2))) {
            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.CREATE_SUBGOAL, user.getId(), goal.getId(), 0));
        }
    }

    private void analyzeSubgoal(Subgoal subgoal) {
        LocalDateTime lastSubgoalActivityDateTime = LocalDateTime.now().minusMonths(4);

        if (lastSubgoalActivityDateTime.isBefore(LocalDateTime.now().minusMonths(3))) {
            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.DELETE_SUBGOAL, user.getId(), subgoal.getGoalId(), subgoal.getId()));
        } else if (lastSubgoalActivityDateTime.isBefore(LocalDateTime.now().minusWeeks(2))) {
            addSuggestiveAction(new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.CREATE_SUBGOAL_FOR_SUBGOAL, user.getId(), subgoal.getGoalId(), subgoal.getId()));
        }
    }

    private void addSuggestiveAction(SuggestiveAction suggestiveAction) {
        suggestiveActions.add(suggestiveAction);
    }
}
