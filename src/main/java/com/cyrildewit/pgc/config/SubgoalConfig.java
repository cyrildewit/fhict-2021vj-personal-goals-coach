package com.cyrildewit.pgc.util;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

//@Configuration
@ConfigurationProperties(prefix = "subgoal")
public class SubgoalConfig
{
    private Integer subgoalLevelIsDeepMinimumLevel;
}