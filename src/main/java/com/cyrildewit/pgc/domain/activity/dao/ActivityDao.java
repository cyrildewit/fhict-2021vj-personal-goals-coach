package com.cyrildewit.pgc.domain.activity.dao;

import java.util.UUID;
import java.util.Optional;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.Model;

public interface ActivityDao {
    public List<Activity> selectAllActivityForSubject(Model subject);

    public List<Activity> selectActivityWithinPeriodForSubjectAndCauser(Model subject, Model causer, LocalDateTime start, LocalDateTime end);

    public Optional<Activity> selectLatestActivityForSubject(Model subject);

    public Optional<Activity> findActivityByUuid(UUID uuid);

    public void insertActivity(Activity activity);

    public boolean updateActivity(Activity activity);

    public long getTotalActiviesCountForSubject(Model subject);

    public long getTotalActiviesCountForSubjectWithinPeriod(Model subject, LocalDateTime start, LocalDateTime end);

    public void truncate();
}