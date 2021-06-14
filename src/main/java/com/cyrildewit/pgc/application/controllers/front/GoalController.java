package com.cyrildewit.pgc.application.controllers.front;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SubgoalDao;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;

import com.cyrildewit.pgc.application.services.GoalService;
import com.cyrildewit.pgc.application.services.SubgoalService;
import com.cyrildewit.pgc.application.services.AuthenticationService;
import com.cyrildewit.pgc.application.services.SuggestiveActionService;
import com.cyrildewit.pgc.application.services.ActivityService;
import com.cyrildewit.pgc.application.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.application.validation.form.goal.CreateGoalFormRequest;
import com.cyrildewit.pgc.application.view_model.front.goal.index.GoalIndexModelAndView;
import com.cyrildewit.pgc.application.view_model.front.goal.index.GoalIndexDto;
import com.cyrildewit.pgc.application.view_model.front.goal.show.GoalShowModelAndView;
import com.cyrildewit.pgc.application.view_model.front.goal.show.GoalShowDto;
import com.cyrildewit.pgc.application.view_model.front.goal.show.GoalShowSubgoalDto;
import com.cyrildewit.pgc.application.view_model.front.goal.edit.GoalEditModelAndView;
import com.cyrildewit.pgc.application.view_model.front.goal.edit.GoalEditDto;

@Controller
@RequestMapping("/goals")
public class GoalController {
    private DateTimeFormatters dateTimeFormatters;
    private final GoalService goalService;
    private final GoalDao goalDao;
    private final SubgoalService subgoalService;
    private final AuthenticationService authenticationService;
    private final SuggestiveActionService suggestiveActionService;
    private final SuggestiveActionDao suggestiveActionDao;
    private final ActivityService activityService;
    private final ActivityDao activityDao;
    private final SubgoalDao subgoalDao;

    @Autowired
    public GoalController(
            GoalService goalService,
            GoalDao goalDao,
            SubgoalService subgoalService,
            AuthenticationService authenticationService,
            SuggestiveActionService suggestiveActionService,
            SuggestiveActionDao suggestiveActionDao,
            ActivityService activityService,
            ActivityDao activityDao,
            SubgoalDao subgoalDao,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.goalService = goalService;
        this.subgoalService = subgoalService;
        this.goalDao = goalDao;
        this.subgoalDao = subgoalDao;
        this.authenticationService = authenticationService;
        this.suggestiveActionService = suggestiveActionService;
        this.suggestiveActionDao = suggestiveActionDao;
        this.activityService = activityService;
        this.activityDao = activityDao;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @GetMapping("")
    public GoalIndexModelAndView index() {
        GoalIndexModelAndView modelAndView = new GoalIndexModelAndView();

        List<GoalIndexDto> goals = new ArrayList<GoalIndexDto>();
        for (Goal goal : goalService.getAllGoalsForUser(authenticationService.getCurrentUser())) {
            goals.add(GoalIndexDto.fromGoalEntity(goal));
        }

        modelAndView.setGoalsList(goals);

        return modelAndView;
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

        activityService.addActivity(new Activity(UUID.randomUUID(), "", "Created goal '" + goalService.getGoalByUuid(goal.getUuid()).get().getTitle() + "'", goal, currentUser));

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
    public GoalShowModelAndView show(@PathVariable("uuid") UUID uuid, Model model) {
        GoalShowModelAndView modelAndView = new GoalShowModelAndView();

        Optional<Goal> optionalGoal = goalService.getGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        List<GoalShowSubgoalDto> subgoals = new ArrayList<GoalShowSubgoalDto>();
        for (Subgoal subgoal : subgoalService.getAllFirstLevelSubgoals(goal)) {
            subgoals.add(GoalShowSubgoalDto.fromSubgoalEntity(subgoal));
        }

        GoalShowDto goalShowDto = GoalShowDto.fromGoalEntity(goal);
        goalShowDto.setSubgoalsCountFormatted(Long.toString(subgoalService.getTotalFirstLevelSubgoalsCountForGoal(goal)));
        goalShowDto.setSuggestiveActionsCountFormatted(Long.toString(suggestiveActionService.getTotalSuggestiveActionsCountForGoal(goal)));

        modelAndView.setGoal(goalShowDto);
        modelAndView.setSubgoalsList(subgoals);

        return modelAndView;
    }

    @GetMapping("/{uuid}/edit")
    public GoalEditModelAndView edit(
            @PathVariable("uuid") UUID uuid,
            Model model
    ) {
        GoalEditModelAndView modelAndView = new GoalEditModelAndView();

        Optional<Goal> optionalGoal = goalService.getGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        if (!model.containsAttribute("goal")) {
            modelAndView.setGoal(GoalEditDto.fromGoalEntity(goal));
        }

        return modelAndView;
    }

    @PutMapping("/{uuid}/edit")
    public String update(
            @PathVariable("uuid") UUID uuid,
            @ModelAttribute("goal") @Valid GoalEditDto formGoal,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Goal> optionalGoal = goalService.getGoalByUuid(uuid);
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
        Optional<Goal> optionalGoal = goalService.getGoalByUuid(uuid);
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
        user.setGoalDao(goalDao);
        user.setSuggestiveActionDao(suggestiveActionDao);
        user.setActivityDao(activityDao);
        user.setSubgoalDao(subgoalDao);
        user.analyzeSuggestiveActions();

        // Redirect with success message

        return "redirect:/goals";
    }
}