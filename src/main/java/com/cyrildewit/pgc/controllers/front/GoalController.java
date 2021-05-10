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
import com.cyrildewit.pgc.services.GoalService;
import com.cyrildewit.pgc.services.SubgoalService;
import com.cyrildewit.pgc.services.AuthenticationService;
import com.cyrildewit.pgc.exceptions.GoalNotFoundException;

@Controller
@RequestMapping("/goals")
public class GoalController {
    @Autowired
    private DateTimeFormatters dateTimeFormatters;

    private final GoalService goalService;
    private final SubgoalService subgoalService;
    private final AuthenticationService authenticationService;

    @Autowired
    public GoalController(
            GoalService goalService,
            SubgoalService subgoalService,
            AuthenticationService authenticationService
    ) {
        this.goalService = goalService;
        this.subgoalService = subgoalService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("goals", goalService.getAllGoalsForUser(authenticationService.getCurrentUser()));

        return "front/goals/index";
    }

    @PostMapping("")
    public String store(@ModelAttribute("goal") @Valid Goal goal, BindingResult result) {
        if (result.hasErrors()) {
            return "front/goals/create";
        }

        long currentUserId = authenticationService.getCurrentUser().getId();

        goal.setUuid(UUID.randomUUID());
        goal.setUserId(currentUserId);
        goalService.addGoal(goal);

        return "redirect:goals/" + goal.getUuid().toString();
    }

    @GetMapping("/create")
    public String create(Goal goal) {
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
            @ModelAttribute("goal") @Valid Goal formGoal,
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

        return "redirect:/goals/" + goal.getUuid().toString();
    }

    @DeleteMapping("/{uuid}")
    public String destroy(@PathVariable("uuid") UUID uuid, Model model) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(uuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(uuid));
        Goal goal = optionalGoal.get();

        goalService.deleteGoal(goal);

        return "redirect:/goals";
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("UUID: " + UUID.randomUUID());
        return "redirect:/goals";

    }
}