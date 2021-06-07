package com.cyrildewit.pgc.application.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;

public interface CoachingStylePreferenceDao
{
    public Optional<CoachingStylePreference> findCoachingSytlePreferenceByGoal(Goal goal);
}