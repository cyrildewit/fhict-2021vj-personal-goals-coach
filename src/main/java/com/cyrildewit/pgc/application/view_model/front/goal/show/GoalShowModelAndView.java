package com.cyrildewit.pgc.application.view_model.front.goal.show;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;

public class GoalShowModelAndView extends ModelAndView {
    private static final String VIEW = "front/goal/show";

    public GoalShowModelAndView() {
        super(VIEW);
    }

    public void setGoal(GoalShowDto goal) {
        this.addObject("goal", goal);
    }

    public void setSubgoalsList(List<GoalShowSubgoalDto> subgoalsList) {
        this.addObject("subgoals", subgoalsList);
    }
}