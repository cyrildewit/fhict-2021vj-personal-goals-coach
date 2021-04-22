package com.cyrildewit.pgc.controllers.front;

import java.util.ArrayList;
import java.util.UUID;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.services.GoalService;

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

}