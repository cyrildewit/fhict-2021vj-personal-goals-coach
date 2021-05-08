package com.cyrildewit.pgc.exceptions;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException
{
    public UserNotFoundException()
    {
        super();
    }

    public UserNotFoundException(long id)
    {
        super("There is no known user with id " + id);
    }

    public UserNotFoundException(UUID uuid)
    {
        super("There is no known user with uuid " + uuid.toString());
    }

//    public static void withEmail(String email)
//    {
//        static("There is no known user with email " + email);
//    }
}