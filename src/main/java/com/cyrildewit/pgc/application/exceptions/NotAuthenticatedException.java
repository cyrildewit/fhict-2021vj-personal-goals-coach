package com.cyrildewit.pgc.application.exceptions;

public class NotAuthenticatedException extends RuntimeException
{
    public NotAuthenticatedException()
    {
        super("There is no user authenticated");
    }
}