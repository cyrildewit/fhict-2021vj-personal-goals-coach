package com.cyrildewit.pgc.controllers.front;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.util.Locale;
import java.time.LocalDateTime;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.Subgoal;
import com.cyrildewit.pgc.services.GoalService;
import com.cyrildewit.pgc.services.SubgoalService;
import com.cyrildewit.pgc.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.exceptions.SubgoalNotFoundException;

@RequestMapping("/goals")
@Controller
public class SubgoalController {
    @Autowired
    private DateTimeFormatters dateTimeFormatters;

    private final GoalService goalService;
    private final SubgoalService subgoalService;

    @Autowired
    public SubgoalController(GoalService goalService, SubgoalService subgoalService) {
        this.goalService = goalService;
        this.subgoalService = subgoalService;
    }

    @PostMapping("{goalUuid}/subgoals")
    public String store(
            @PathVariable("goalUuid") UUID goalUuid,
            @ModelAttribute("subgoal") @Valid Subgoal subgoal,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.subgoal", result);
            redirectAttributes.addFlashAttribute("subgoal", subgoal);

            return "redirect:/goals/" + goal.getUuid() + "/subgoals/create";
        }

        subgoal.setId(4L);
        subgoal.setUuid(UUID.randomUUID());
        subgoalService.addSubgoal(subgoal);

        return "redirect:/goals/" + goal.getUuid().toString() + "/subgoals/" + subgoal.getUuid().toString();
    }

    @PostMapping("{goalUuid}/subgoals/{parentSubgoalUuid}/subgoals")
    public String storeChildSubgoal(
            @PathVariable("goalUuid") UUID goalUuid,
            @PathVariable("parentSubgoalUuid") UUID parentSubgoalUuid,
            @ModelAttribute("subgoal") @Valid Subgoal subgoal,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalParentSubgoal = subgoalService.findSubgoalByUuid(parentSubgoalUuid);
        optionalParentSubgoal.orElseThrow(() -> new SubgoalNotFoundException(parentSubgoalUuid));
        Subgoal parentSubgoal = optionalParentSubgoal.get();

        if (!subgoalService.determineIfSubgoalBelongsToGoal(subgoal, goal)) {
            throw new SubgoalNotFoundException(parentSubgoalUuid);
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.subgoal", result);
            redirectAttributes.addFlashAttribute("subgoal", subgoal);

            return "redirect:/goals/" + goal.getUuid() + "/subgoals/" + parentSubgoal.getUuid() + "/subgoals/create";
        }

        subgoal.setId(4L);
        subgoal.setUuid(UUID.randomUUID());

        subgoalService.addSubgoal(subgoal);

        return "redirect:/goals/" + goal.getUuid().toString() + "/subgoals/" + subgoal.getUuid().toString();
    }

    @GetMapping("{goalUuid}/subgoals/create")
    public String create(@PathVariable("goalUuid") UUID goalUuid, Model model) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        model.addAttribute("goal", goal);
        if (!model.containsAttribute("subgoal")) {
            model.addAttribute("subgoal", new Subgoal());
        }

        return "front/goals/subgoals/create";
    }

    @GetMapping("{goalUuid}/subgoals/{parentSubgoalUuid}/subgoals/create")
    public String create(@PathVariable("goalUuid") UUID goalUuid, @PathVariable("parentSubgoalUuid") UUID parentSubgoalUuid, Model model) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalParentSubgoal = subgoalService.findSubgoalByUuid(parentSubgoalUuid);
        optionalParentSubgoal.orElseThrow(() -> new SubgoalNotFoundException(parentSubgoalUuid));
        Subgoal parentSubgoal = optionalParentSubgoal.get();

        if (!subgoalService.determineIfSubgoalBelongsToGoal(parentSubgoal, goal)) {
            throw new SubgoalNotFoundException(parentSubgoalUuid);
        }

        model.addAttribute("goal", goal);
        if (!model.containsAttribute("subgoal")) {
            model.addAttribute("subgoal", new Subgoal());
        }

        model.addAttribute("parentSubgoal", parentSubgoal);

        return "front/goals/subgoals/subgoals/create";
    }

    @GetMapping("/{goalUuid}/subgoals/{subgoalUuid}")
    public String show(@PathVariable("goalUuid") UUID goalUuid, @PathVariable("subgoalUuid") UUID subgoalUuid, Model model) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalSubgoal = subgoalService.findSubgoalByUuid(subgoalUuid);
        optionalSubgoal.orElseThrow(() -> new SubgoalNotFoundException(subgoalUuid));
        Subgoal subgoal = optionalSubgoal.get();

        if (!subgoalService.determineIfSubgoalBelongsToGoal(subgoal, goal)) {
            throw new SubgoalNotFoundException(subgoalUuid);
        }

        List<Subgoal> subgoals = subgoalService.getAllSubgoalsForSubgoal(subgoal);

        model.addAttribute("goal", goal);
        model.addAttribute("subgoal", subgoal);
        model.addAttribute("subgoals", subgoals);
        model.addAttribute("subgoalsCountFormatted", subgoalService.getTotalSubgoalsCountForSubgoal(subgoal));
        model.addAttribute("subgoalDeadlineFormatted", subgoal.getDeadline().format(dateTimeFormatters.getDayMonthYearFormatter()));

        return "front/goals/subgoals/show";
    }
}