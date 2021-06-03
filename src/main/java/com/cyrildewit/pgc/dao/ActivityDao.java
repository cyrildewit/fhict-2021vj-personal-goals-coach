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
import com.cyrildewit.pgc.model.Model;

public interface ActivityDao
{
    public List<Activity> selectAllActvityForSubject(Model subject);

    public List<Activity> selectActvityWithinPeriodForSubjectAndCauser(Model subject, Model causer, LocalDateTime start, LocalDateTime end);

    public void insertActivity(Activity activity);
}