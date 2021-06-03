package com.cyrildewit.pgc.services;

import java.util.List;

import com.cyrildewit.pgc.model.Activity;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.model.Goal;

import com.cyrildewit.pgc.model.Model;

public interface ActivityServiceInterface
{
    public void addActivity(Activity activity);

    public List<Activity> getAllActvityForSubject(Model subject);
}
