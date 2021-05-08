package com.cyrildewit.pgc.exceptions;

public class NotAuthenticatedException extends RuntimeException
{
    public NotAuthenticatedException()
    {
        super("There is no user authenticated");
    }
}