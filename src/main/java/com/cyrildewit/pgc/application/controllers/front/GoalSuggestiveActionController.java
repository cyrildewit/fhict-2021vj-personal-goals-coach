package com.cyrildewit.pgc.application.controllers.front;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.application.services.GoalService;
import com.cyrildewit.pgc.application.services.SubgoalService;
import com.cyrildewit.pgc.application.services.AuthenticationService;
import com.cyrildewit.pgc.application.services.SuggestiveActionService;
import com.cyrildewit.pgc.application.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.application.validation.form.goal.CreateGoalFormRequest;

@Controller
@RequestMapping("/goals")
public class GoalSuggestiveActionController {
    private DateTimeFormatters dateTimeFormatters;
    private final GoalService goalService;
    private final SubgoalService subgoalService;
    private final AuthenticationService authenticationService;
    private final SuggestiveActionService suggestiveActionService;

    @Autowired
    public GoalSuggestiveActionController(
            GoalService goalService,
            SubgoalService subgoalService,
            AuthenticationService authenticationService,
            SuggestiveActionService suggestiveActionService,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.goalService = goalService;
        this.subgoalService = subgoalService;
        this.authenticationService = authenticationService;
        this.suggestiveActionService = suggestiveActionService;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @GetMapping("/{uuid}/suggestive-actions")
    public String index(
            @PathVariable("uuid") UUID uuid,
            Model model
    ) {
        Optional<Goal> optionalGoal = goalService.getGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        List<SuggestiveAction> suggestiveActions = suggestiveActionService.getAllSuggestiveActionsForGoal(goal);
        List<Long> goalIds = suggestiveActions
                    .stream()
                    .map(suggestiveAction -> new Long(suggestiveAction.getGoalId()))
                    .collect(Collectors.toList());

        List<Goal> goals = goalService.getGoalsByIds(goalIds);

        model.addAttribute("goal", goal);
        model.addAttribute("goals", goals);
        model.addAttribute("suggestiveActions", suggestiveActions);
        model.addAttribute("suggestiveActionsCountFormatted", suggestiveActionService.getTotalSuggestiveActionsCountForGoal(goal));

        return "front/goals/suggestive-action/index";
    }

    public String index(Model model) {
        return "front/goals/index";
    }
}