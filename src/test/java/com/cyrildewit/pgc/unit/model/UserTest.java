package com.cyrildewit.pgc.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;

import com.cyrildewit.pgc.domain.user.model.User;

class UserTest {
    @Test
    void getFullNameReturnsFullName() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        assertEquals("John Doe", user.getFullName());
    }

    @Test
    void getFullNameDoesNotIncludeNullOrEmptyValues() {
        User user = new User();

        assertEquals("", user.getFullName());
    }
}
