package com.cyrildewit.pgc.application.view_model.front.goal.edit;

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
import org.springframework.web.servlet.ModelAndView;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;
import com.cyrildewit.pgc.application.services.GoalService;
import com.cyrildewit.pgc.application.services.SubgoalService;
import com.cyrildewit.pgc.application.services.AuthenticationService;
import com.cyrildewit.pgc.application.services.SuggestiveActionService;
import com.cyrildewit.pgc.application.services.ActivityService;
import com.cyrildewit.pgc.application.exceptions.GoalNotFoundException;
import com.cyrildewit.pgc.application.validation.form.goal.CreateGoalFormRequest;

public class GoalEditModelAndView extends ModelAndView {
    private static final String VIEW = "front/goal/edit";

    public GoalEditModelAndView() {
        super(VIEW);
    }

    public void setGoal(GoalEditDto goal) {
        this.addObject("goal", goal);
    }
}