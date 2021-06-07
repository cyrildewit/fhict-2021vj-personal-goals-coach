package com.cyrildewit.pgc.application.controllers.front;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Value;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;

import com.cyrildewit.pgc.application.services.GoalService;
import com.cyrildewit.pgc.application.services.SubgoalService;
import com.cyrildewit.pgc.application.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.application.exceptions.SubgoalNotFoundException;
import com.cyrildewit.pgc.application.enums.SubgoalParentType;
import com.cyrildewit.pgc.application.validation.form.subgoal.CreateSubgoalFormRequest;

@RequestMapping("/goals")
@Controller
public class SubgoalController {
    @Autowired
    private DateTimeFormatters dateTimeFormatters;

    @Value("${subgoal.subgoalLevelIsDeepMinimumLevel}")
    private Integer subgoalLevelIsDeepMinimumLevel;

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
            @ModelAttribute("subgoal") @Valid CreateSubgoalFormRequest subgoalForm,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.subgoal", result);
            redirectAttributes.addFlashAttribute("subgoal", subgoalForm);

            return "redirect:/goals/" + goal.getUuid() + "/subgoals/create";
        }

        Subgoal subgoal = new Subgoal(UUID.randomUUID(), subgoalForm.getTitle(), subgoalForm.getDescription(), subgoalForm.getDeadline(), 0, 0);
        subgoalService.addSubgoal(subgoal);

        return "redirect:/goals/" + goal.getUuid().toString() + "/subgoals/" + subgoal.getUuid().toString();
    }

    @PostMapping("{goalUuid}/subgoals/{parentSubgoalUuid}/subgoals")
    public String storeChildSubgoal(
            @PathVariable("goalUuid") UUID goalUuid,
            @PathVariable("parentSubgoalUuid") UUID parentSubgoalUuid,
            @ModelAttribute("subgoal") @Valid CreateSubgoalFormRequest subgoalForm,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalParentSubgoal = subgoalService.findSubgoalByUuid(parentSubgoalUuid);
        optionalParentSubgoal.orElseThrow(() -> new SubgoalNotFoundException(parentSubgoalUuid));
        Subgoal parentSubgoal = optionalParentSubgoal.get();

        if (!subgoalService.determineIfSubgoalBelongsToGoal(parentSubgoal, goal)) {
            throw new SubgoalNotFoundException(parentSubgoalUuid);
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.subgoal", result);
            redirectAttributes.addFlashAttribute("subgoal", subgoalForm);

            return "redirect:/goals/" + goal.getUuid() + "/subgoals/" + parentSubgoal.getUuid() + "/subgoals/create";
        }

        Subgoal subgoal = new Subgoal(UUID.randomUUID(), subgoalForm.getTitle(), subgoalForm.getDescription(), subgoalForm.getDeadline(), goal.getId(), parentSubgoal.getId());
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
    public String createChildSubgoal(@PathVariable("goalUuid") UUID goalUuid, @PathVariable("parentSubgoalUuid") UUID parentSubgoalUuid, Model model) {
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

        boolean parentSubgoalIsWithinDeepLevel = subgoalService.determineSubgoalLevel(parentSubgoal) >= subgoalLevelIsDeepMinimumLevel;
        model.addAttribute("parentSubgoalIsWithinDeepLevel", parentSubgoalIsWithinDeepLevel);

        model.addAttribute("parentSubgoal", parentSubgoal);

        return "front/goals/subgoals/subgoals/create";
    }

    @GetMapping("/{goalUuid}/subgoals/{subgoalUuid}")
    public String show(
            @PathVariable("goalUuid") UUID goalUuid,
            @PathVariable("subgoalUuid") UUID subgoalUuid,
            Model model
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalSubgoal = subgoalService.findSubgoalByUuid(subgoalUuid);
        optionalSubgoal.orElseThrow(() -> new SubgoalNotFoundException(subgoalUuid));
        Subgoal subgoal = optionalSubgoal.get();

        if (!subgoalService.determineIfSubgoalBelongsToGoal(subgoal, goal)) {
            throw new SubgoalNotFoundException(subgoalUuid);
        }

        SubgoalParentType subgoalParentType = subgoal.getParentSubgoalId() == 0 ? SubgoalParentType.GOAL : SubgoalParentType.SUBGOAL;

        System.out.println("HIER:: " + subgoalParentType + " :::: " + subgoal.getParentSubgoalId());

        if (subgoalParentType == SubgoalParentType.SUBGOAL) {
            Optional<Subgoal> optionalParentSubgoal = subgoalService.findSubgoalById(subgoal.getParentSubgoalId());
            optionalParentSubgoal.orElseThrow(() -> new SubgoalNotFoundException(subgoal.getParentSubgoalId()));
            Subgoal parentSubgoal = optionalParentSubgoal.get();

            model.addAttribute("parentSubgoal", parentSubgoal);
        }

        List<Subgoal> subgoals = subgoalService.getAllSubgoalsForSubgoal(subgoal);

        model.addAttribute("goal", goal);
        model.addAttribute("subgoal", subgoal);
        model.addAttribute("subgoals", subgoals);
        model.addAttribute("subgoalsCountFormatted", subgoalService.getTotalSubgoalsCountForSubgoal(subgoal));
        model.addAttribute("subgoalDeadlineFormatted", subgoal.getDeadline().format(dateTimeFormatters.getDayMonthYearFormatter()));
        model.addAttribute("subgoalParentType", subgoalParentType);

        return "front/goals/subgoals/show";
    }

    @GetMapping("/{goalUuid}/subgoals/{subgoalUuid}/edit")
    public String edit(
            @PathVariable("goalUuid") UUID goalUuid,
            @PathVariable("subgoalUuid") UUID subgoalUuid,
            Model model
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalSubgoal = subgoalService.findSubgoalByUuid(subgoalUuid);
        optionalSubgoal.orElseThrow(() -> new SubgoalNotFoundException(subgoalUuid));
        Subgoal subgoal = optionalSubgoal.get();

        if (!subgoalService.determineIfSubgoalBelongsToGoal(subgoal, goal)) {
            throw new SubgoalNotFoundException(subgoalUuid);
        }

        model.addAttribute("goal", goal);
        model.addAttribute("subgoal", subgoal);

        return "front/goals/subgoals/edit";
    }

    @PutMapping("/{goalUuid}/subgoals/{subgoalUuid}/edit")
    public String update(
            @PathVariable("goalUuid") UUID goalUuid,
            @PathVariable("subgoalUuid") UUID subgoalUuid,
            @ModelAttribute("subgoal") @Valid Subgoal formSubgoal,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalSubgoal = subgoalService.findSubgoalByUuid(subgoalUuid);
        optionalSubgoal.orElseThrow(() -> new SubgoalNotFoundException(subgoalUuid));
        Subgoal subgoal = optionalSubgoal.get();

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.subgoal", result);
            redirectAttributes.addFlashAttribute("subgoal", formSubgoal);

            return "redirect:/goals/" + goal.getUuid() + "/subgoals/" + subgoal.getUuid() + "/edit";
        }

        subgoal.setTitle(formSubgoal.getTitle());
        subgoal.setDescription(formSubgoal.getDescription());
        subgoal.setDeadline(formSubgoal.getDeadline());

        subgoalService.updateSubgoal(subgoal);

        return "redirect:/goals/" + goal.getUuid() + "/subgoals/" + subgoal.getUuid();
    }

    @DeleteMapping("/{goalUuid}/subgoals/{subgoalUuid}")
    public String destroy(
            @PathVariable("goalUuid") UUID goalUuid,
            @PathVariable("subgoalUuid") UUID subgoalUuid
    ) {
        Optional<Goal> optionalGoal = goalService.findGoalByUuid(goalUuid);
        optionalGoal.orElseThrow(() -> new GoalNotFoundException(goalUuid));
        Goal goal = optionalGoal.get();

        Optional<Subgoal> optionalSubgoal = subgoalService.findSubgoalByUuid(subgoalUuid);
        optionalSubgoal.orElseThrow(() -> new SubgoalNotFoundException(subgoalUuid));
        Subgoal subgoal = optionalSubgoal.get();

        if (!subgoalService.determineIfSubgoalBelongsToGoal(subgoal, goal)) {
            throw new SubgoalNotFoundException(subgoalUuid);
        }

        subgoalService.deleteSubgoal(subgoal);

        return "redirect:/goals/" + goal.getUuid();
    }
}