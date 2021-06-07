package com.cyrildewit.pgc.application.services;

import java.util.List;
import java.util.Optional;

import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.goal.model.Goal;

import com.cyrildewit.pgc.domain.Model;

public interface ActivityServiceInterface
{
    public void addActivity(Activity activity);

    public List<Activity> getAllActivityForSubject(Model subject);

    public Optional<Activity> selectLatestActivityForSubject(Model subject);
}
