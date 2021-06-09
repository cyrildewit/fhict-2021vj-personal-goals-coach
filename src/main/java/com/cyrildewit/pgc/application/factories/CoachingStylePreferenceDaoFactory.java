package com.cyrildewit.pgc.application.factories;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.application.dao.SqlCoachingStylePreferenceDao;
import com.cyrildewit.pgc.application.dao.CoachingStylePreferenceDao;

public class CoachingStylePreferenceDaoFactory
{
    public static CoachingStylePreferenceDao getSqlCoachingStylePreferenceDaoFactory()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(SqlCoachingStylePreferenceDao.class);

        return context.getBean(SqlCoachingStylePreferenceDao.class);
    }
}