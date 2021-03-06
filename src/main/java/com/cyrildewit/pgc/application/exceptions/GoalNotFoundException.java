package com.cyrildewit.pgc.application.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GoalNotFoundException extends RuntimeException
{
    public GoalNotFoundException(UUID uuid)
    {
        super("There is no known goal with uuid " + uuid.toString());
    }
}