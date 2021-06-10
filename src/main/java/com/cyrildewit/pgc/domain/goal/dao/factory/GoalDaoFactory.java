package com.cyrildewit.pgc.domain.goal.dao.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SqlGoalDao;

public class GoalDaoFactory
{
    public static GoalDao getSqlGoalDao()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(SqlGoalDao.class);

        return context.getBean(SqlGoalDao.class);
    }
}