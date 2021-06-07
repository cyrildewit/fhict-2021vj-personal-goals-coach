package com.cyrildewit.pgc.application.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubgoalNotFoundException extends RuntimeException
{
    public SubgoalNotFoundException(long id)
    {
        super("There is no known subgoal with id " + id);
    }

    public SubgoalNotFoundException(UUID uuid)
    {
        super("There is no known subgoal with uuid " + uuid.toString());
    }
}