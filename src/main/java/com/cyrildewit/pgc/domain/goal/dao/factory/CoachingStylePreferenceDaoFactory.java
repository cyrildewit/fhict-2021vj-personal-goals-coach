package com.cyrildewit.pgc.domain.goal.dao.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.dao.SqlCoachingStylePreferenceDao;
import com.cyrildewit.pgc.domain.goal.dao.CoachingStylePreferenceDao;

public class CoachingStylePreferenceDaoFactory
{
    public static CoachingStylePreferenceDao getSqlCoachingStylePreferenceDaoFactory()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(SqlCoachingStylePreferenceDao.class);

        return context.getBean(SqlCoachingStylePreferenceDao.class);
    }
}