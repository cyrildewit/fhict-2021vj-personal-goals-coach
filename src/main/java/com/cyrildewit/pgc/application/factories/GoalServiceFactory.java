package com.cyrildewit.pgc.application.factories;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cyrildewit.pgc.application.services.GoalService;
import com.cyrildewit.pgc.application.services.GoalServiceInterface;

public class GoalServiceFactory
{
    public static GoalServiceInterface getGoalService()
    {
        ApplicationContext context = new AnnotationConfigApplicationContext(GoalService.class);

        return context.getBean(GoalService.class);
    }
}