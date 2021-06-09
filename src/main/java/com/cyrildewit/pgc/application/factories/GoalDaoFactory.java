package com.cyrildewit.pgc.application.factories;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.application.dao.GoalDao;
import com.cyrildewit.pgc.application.dao.SqlGoalDao;

public class GoalDaoFactory
{
    public static GoalDao getSqlGoalDao()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(SqlGoalDao.class);

        return context.getBean(SqlGoalDao.class);
    }
}