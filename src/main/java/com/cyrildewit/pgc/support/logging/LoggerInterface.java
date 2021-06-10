package com.cyrildewit.pgc.support.logging;

public interface LoggerInterface
{
    public void trace(String message);

    public void debug(String message);

    public void info(String message);

    public void warn(String message);

    public void error(String message);
}