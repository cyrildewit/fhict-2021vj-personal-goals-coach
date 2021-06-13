package com.cyrildewit.pgc.domain.suggestive_action.dao.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SqlSuggestiveActionDao;
import com.cyrildewit.pgc.domain.suggestive_action.dao.SuggestiveActionDao;

public class SuggestiveActionDaoFactory
{
    public static SuggestiveActionDao getSqlSuggestiveActionDaoFactory()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(SqlSuggestiveActionDao.class);

        return context.getBean(SqlSuggestiveActionDao.class);
    }
}