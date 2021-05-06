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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;

import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.Subgoal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.services.GoalService;
import com.cyrildewit.pgc.services.SubgoalService;
import com.cyrildewit.pgc.exceptions.GoalNotFoundException;

@Controller
public class DashboardController {
    private final GoalService goalService;
    private final SubgoalService subgoalService;

    @Autowired
    public DashboardController(GoalService goalService, SubgoalService subgoalService) {
        this.goalService = goalService;
        this.subgoalService = subgoalService;
    }

    @GetMapping("/")
    public String show(Model model) {
        User user = new User();
        model.addAttribute("goalsCountFormatted", goalService.getTotalGoalsCountForUser(user));

        return "front/dashboard/index";
    }
}