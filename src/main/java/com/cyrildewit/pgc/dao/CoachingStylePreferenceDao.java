package com.cyrildewit.pgc.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.model.Activity;
import com.cyrildewit.pgc.model.CoachingStylePreference;

public interface CoachingStylePreferenceDao
{
    public Optional<CoachingStylePreference> findCoachingSytlePreferenceByGoal(Goal goal);
}