package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.Model;

import com.cyrildewit.pgc.application.dao.ActivityDao;
import com.cyrildewit.pgc.application.dao.SqlActivityDao;

@Service
public class ActivityService implements ActivityServiceInterface {
    private final ActivityDao activityDao;

    @Autowired
    public ActivityService(SqlActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void addActivity(Activity activity)
    {
        activityDao.insertActivity(activity);
    }

    public Optional<Activity> selectLatestActivityForSubject(Model subject)
    {
        return activityDao.selectLatestActivityForSubject(subject);
    }

    public List<Activity> getAllActivityForSubject(Model subject)
    {
        return activityDao.selectAllActivityForSubject(subject);
    }
}
