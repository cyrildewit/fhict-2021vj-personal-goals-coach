package com.cyrildewit.pgc.application.view_model.front.goal.index;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.servlet.ModelAndView;

import com.cyrildewit.pgc.domain.goal.model.Goal;

public class GoalIndexModelAndView extends ModelAndView {
    private static final String VIEW = "front/goal/index";

    public GoalIndexModelAndView() {
        super(VIEW);
    }

    public void setGoalsList(List<GoalIndexDto> goalsList) {
        this.addObject("goals", goalsList);
    }
}