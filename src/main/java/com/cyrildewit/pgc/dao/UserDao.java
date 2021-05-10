package com.cyrildewit.pgc.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import com.cyrildewit.pgc.model.User;

public interface UserDao {
    public List<User> selectAllUsers();

    public Optional<User> findUserById(long id);

    public Optional<User> findUserByUuid(UUID uuid);

    public Optional<User> findUserByEmail(String email);

    public void insertUser(User user);

    public boolean updateUser(User user);

    public void deleteUserById(long id);

    public void deleteUser(User user);
}