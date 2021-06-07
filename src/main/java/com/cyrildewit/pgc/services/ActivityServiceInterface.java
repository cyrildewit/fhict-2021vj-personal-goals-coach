package com.cyrildewit.pgc.services;

import java.util.List;
import java.util.Optional;

import com.cyrildewit.pgc.logic.model.Activity;
import com.cyrildewit.pgc.logic.model.User;
import com.cyrildewit.pgc.logic.model.Goal;

import com.cyrildewit.pgc.logic.model.Model;

public interface ActivityServiceInterface
{
    public void addActivity(Activity activity);

    public List<Activity> getAllActvityForSubject(Model subject);

    public Optional<Activity> selectLatestActivityForSubject(Model subject);
}
