package com.cyrildewit.pgc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.Activity;
import com.cyrildewit.pgc.model.Model;
import com.cyrildewit.pgc.dao.ActivityDao;
import com.cyrildewit.pgc.dao.SqlActivityDao;

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

    public List<Activity> getAllActvityForSubject(Model subject)
    {
        return activityDao.selectAllActvityForSubject(subject);
    }
}
