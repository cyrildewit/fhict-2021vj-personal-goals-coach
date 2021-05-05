package com.cyrildewit.pgc.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.stereotype.Component;

import com.cyrildewit.pgc.models.User;

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
                "password"
        ));

        users.add(new User(
                1L,
                UUID.fromString("2fa2bee2-968c-4de6-a171-989560d80701"),
                "John",
                "Doe",
                "+31 6 2772 3737",
                "jane@example.com",
                LocalDateTime.now(),
                "password"
        ));
    }

    public List<User> selectAllUsers() {
        return users;
    }

    public Optional<User> findUserById(Long id) {
        return users.stream()
                .filter(user -> id.equals(user.getId()))
                .findAny();
    }

    public Optional<User> findUserByUuid(UUID uuid) {
        return users.stream()
                .filter(user -> uuid.equals(user.getUuid()))
                .findAny();
    }

    public void insertUser(User user) {
        users.add(user);
    }

    public void updateUser(User user) {
        users.add(user);
    }

    public void deleteUserById(Long id) {
        Optional<User> user = users.stream()
                .filter(streamUser -> id.equals(streamUser.getId()))
                .findAny();

        if (user.isPresent()) {
            users.remove(user);
        }
    }

    public void deleteUser(User user) {
        users.remove(user);
    }

    public Long getTotalUsersCountForUser(User user)
    {
        return users.stream().count();
    }
}