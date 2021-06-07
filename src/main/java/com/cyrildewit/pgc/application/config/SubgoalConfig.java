package com.cyrildewit.pgc.util.application;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "subgoal")
public class SubgoalConfig
{
    private Integer subgoalLevelIsDeepMinimumLevel;
}