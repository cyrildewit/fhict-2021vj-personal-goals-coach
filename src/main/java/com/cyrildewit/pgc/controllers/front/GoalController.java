package com.cyrildewit.pgc.controllers.front;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

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

import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.Subgoal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.model.Activity;
import com.cyrildewit.pgc.model.SuggestiveAction;
import com.cyrildewit.pgc.services.GoalService;
import com.cyrildewit.pgc.services.SubgoalService;
import com.cyrildewit.pgc.services.AuthenticationService;
import com.cyrildewit.pgc.services.SuggestiveActionService;
import com.cyrildewit.pgc.services.ActivityService;
import com.cyrildewit.pgc.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.validation.form.goal.CreateGoalFormRequest;
import com.cyrildewit.pgc.validation.form.goal.UpdateGoalFormRequest;

@Controller
@RequestMapping("/goals")
public class GoalController {
    private DateTimeFormatters dateTimeFormatters;
    private final GoalService goalService;
    private final SubgoalService subgoalService;
    private final AuthenticationService authenticationService;
    private final SuggestiveActionService suggestiveActionService;
    private final ActivityService activityService;

    @Autowired
    public GoalController(
            GoalService goalService,
            SubgoalService subgoalService,
            AuthenticationService authenticationService,
            SuggestiveActionService suggestiveActionService,
            ActivityService activityService,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.goalService = goalService;
        this.subgoalService = subgoalService;
        this.authenticationService = authenticationService;
        this.suggestiveActionService = suggestiveActionService;
        this.activityService = activityService;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("goals", goalService.getAllGoalsForUser(authenticationService.getCurrentUser()));

        return "front/goals/index";
    }

    @PostMapping("")
    public String store(
            @ModelAttribute("goal") @Valid CreateGoalFormRequest goalForm,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.goal", result);
            redirectAttributes.addFlashAttribute("goal", goalForm);

            return "redirect:/goals/create";
        }

        User currentUser = authenticationService.getCurrentUser();

        Goal goal = new Goal(UUID.randomUUID(), goalForm.getTitle(), goalForm.getDescription(), goalForm.getDeadline(), currentUser.getId());
        goalService.addGoal(goal);

        activityService.addActivity(new Activity(UUID.randomUUID(), "", "Created goal '" + goalService.findGoalByUuid(goal.getUuid()).get().getTitle() + "'", goal, currentUser));

        return "redirect:/goals/" + goal.getUuid().toString();
    }

    @GetMapping("/create")
    public String create(Model model) {
        if (!model.containsAttribute("goal")) {
            model.addAttribute("goal", new Goal());
        }

        return "front/goals/create";
    }

    @GetMapping("/{uuid}")
    public String show(@PathVariable("uuid") UUID uuid, Model model) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        model.addAttribute("goal", goal);

        List<Subgoal> subgoals = subgoalService.getAllFirstLevelSubgoals(goal);
        model.addAttribute("subgoals", subgoals);

        model.addAttribute("subgoalsCountFormatted", subgoalService.getTotalFirstLevelSubgoalsCountForGoal(goal));
        model.addAttribute("goalDeadlineFormatted", goal.getDeadline().format(dateTimeFormatters.getDayMonthYearFormatter()));
        model.addAttribute("suggestiveActionsCountFormatted", suggestiveActionService.getTotalSuggestiveActionsCountForGoal(goal));

        return "front/goals/show";
    }

    @GetMapping("/{uuid}/edit")
    public String edit(
            @PathVariable("uuid") UUID uuid,
            Model model
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        if (!model.containsAttribute("goal")) {
            model.addAttribute("goal", goal);
        }

        return "front/goals/edit";
    }

    @PutMapping("/{uuid}/edit")
    public String update(
            @PathVariable("uuid") UUID uuid,
            @ModelAttribute("goal") @Valid UpdateGoalFormRequest formGoal,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.goal", result);
            redirectAttributes.addFlashAttribute("goal", formGoal);

            return "redirect:/goals/" + goal.getUuid() + "/edit";
        }

        goal.setTitle(formGoal.getTitle());
        goal.setDescription(formGoal.getDescription());
        goal.setDeadline(formGoal.getDeadline());

        goalService.updateGoal(goal);

        User currentUser = authenticationService.getCurrentUser();

        activityService.addActivity(new Activity(UUID.randomUUID(), "", "Updated goal '" + goal.getTitle() + "'", goal, currentUser));

        return "redirect:/goals/" + goal.getUuid().toString();
    }

    @DeleteMapping("/{uuid}")
    public String destroy(@PathVariable("uuid") UUID uuid, Model model) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        goalService.deleteGoal(goal);

        User currentUser = authenticationService.getCurrentUser();

        activityService.addActivity(new Activity(UUID.randomUUID(), "", "Deleted goal '" + goal.getTitle() + "'", goal, currentUser));

        return "redirect:/goals";
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("UUID: " + UUID.randomUUID());
        return "redirect:/goals";

    }

    @GetMapping("/analyze-suggestive-actions")
    public String analyzeSuggestiveActions() {
        User user = authenticationService.getCurrentUser();
        user.setGoals(goalService.getAllGoalsForUser(user));
        List<List> suggestiveActionsAggregation = new ArrayList<List>();

        for (Goal goal : user.getGoals()) {
            for (SuggestiveAction suggestiveAction : goal.analyzeSuggestiveActions()) {
                suggestiveActionService.addUniqueSuggestiveAction(suggestiveAction);
            }
        }

        // Redirect with success message

        return "redirect:/goals";
    }
}