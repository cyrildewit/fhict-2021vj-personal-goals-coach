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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.BindingResult;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.application.services.GoalService;
import com.cyrildewit.pgc.application.services.SubgoalService;
import com.cyrildewit.pgc.application.services.AuthenticationService;
import com.cyrildewit.pgc.application.services.SuggestiveActionService;
import com.cyrildewit.pgc.application.exceptions.GoalNotFoundException;

@Controller
public class DashboardController {
    private final GoalService goalService;
    private final SubgoalService subgoalService;
    private final AuthenticationService authenticationService;
    private final SuggestiveActionService suggestiveActionService;

    @Autowired
    public DashboardController(
            GoalService goalService,
            SubgoalService subgoalService,
            AuthenticationService authenticationService,
            SuggestiveActionService suggestiveActionService
    ) {
        this.goalService = goalService;
        this.subgoalService = subgoalService;
        this.authenticationService = authenticationService;
        this.suggestiveActionService = suggestiveActionService;
    }

    @GetMapping("/")
    public String show(Model model) {
        model.addAttribute("goalsCountFormatted", goalService.getTotalGoalsCountForUser(authenticationService.getCurrentUser()));
        model.addAttribute("suggestiveActionsCountFormatted", suggestiveActionService.getTotalSuggestiveActionsCountForUser(authenticationService.getCurrentUser()));

        return "front/dashboard/index";
    }
}