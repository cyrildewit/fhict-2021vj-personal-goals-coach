package com.cyrildewit.pgc.controllers.front;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
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

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.services.GoalService;
import com.cyrildewit.pgc.exceptions.GoalNotFoundException;

@Controller
@RequestMapping("/goals")
public class GoalController {
    private final GoalService goalService;

    @Autowired
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @GetMapping("")
    public String index(Model model) {
        model.addAttribute("goals", goalService.getAllGoals());

        return "front/goals/index";
    }

    @PostMapping("")
    public String store(@ModelAttribute("goal") @Valid Goal goal, BindingResult result) {
        if (result.hasErrors()) {
            return "front/goals/create";
        }

        goal.setId(4L);
        goal.setUuid(UUID.randomUUID());

        goalService.addGoal(goal);

        return "redirect:goals";
    }

    @GetMapping("/create")
    public String create(Goal goal) {
        return "front/goals/create";
    }

    @GetMapping("/{uuid}")
    public String show(@PathVariable("uuid") UUID uuid, Model model) {
        Optional<Goal> goal = goalService.findGoalByUuid(uuid);

        goal.orElseThrow(() -> new GoalNotFoundException(uuid));

        model.addAttribute("goal", goal.get());

        return "front/goals/show";
    }
}