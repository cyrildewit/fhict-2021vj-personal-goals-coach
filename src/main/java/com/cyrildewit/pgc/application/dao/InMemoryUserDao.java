package com.cyrildewit.pgc.application.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.stereotype.Component;

import com.cyrildewit.pgc.domain.user.model.User;

@Component
public class InMemoryUserDao implements UserDao {
    private List<User> users = new ArrayList<>();

    public InMemoryUserDao() {
        users.add(new User(
                1L,
                UUID.fromString("2fa2bee2-968c-4de6-a171-989560d80701"),
                "Jane",
                "Doe",
                "+31 6 2772 3737",
                "jane@example.com",
                LocalDateTime.now(),
                "password",
                LocalDateTime.now(),
                LocalDateTime.now()
        ));

        users.add(new User(
                1L,
                UUID.fromString("2fa2bee2-968c-4de6-a171-989560d80701"),
                "John",
                "Doe",
                "+31 6 2772 3737",
                "jane@example.com",
                LocalDateTime.now(),
                "password",
                LocalDateTime.now(),
                LocalDateTime.now()
        ));
    }

    public List<User> selectAllUsers() {
        return users;
    }

    public Optional<User> findUserById(long id) {
        return users.stream()
                .filter(user -> id == user.getId())
                .findAny();
    }

    public Optional<User> findUserByUuid(UUID uuid) {
        return users.stream()
                .filter(user -> uuid.equals(user.getUuid()))
                .findAny();
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream()
                .filter(user -> email.equals(user.getEmail()))
                .findAny();
    }

    public void insertUser(User user) {
        users.add(user);
    }

    public boolean updateUser(User user) {
        users.add(user);

        return true;
    }

    public void deleteUserById(long id) {
        Optional<User> user = users.stream()
                .filter(streamUser -> id == streamUser.getId())
                .findAny();

        if (user.isPresent()) {
            users.remove(user);
        }
    }

    public void deleteUser(User user) {
        users.remove(user);
    }

    public long getTotalUsersCountForUser(User user) {
        return users.stream().count();
    }
}