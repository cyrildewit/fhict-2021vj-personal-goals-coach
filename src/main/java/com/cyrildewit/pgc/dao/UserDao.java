package com.cyrildewit.pgc.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import com.cyrildewit.pgc.models.User;

public interface UserDao {
    public List<User> selectAllUsers();

    public Optional<User> findUserById(Integer id);

    public Optional<User> findUserByUuid(UUID uuid);

    public void insertUser(User user);

    public void updateUser(User user, String[] params);

    public void deleteUserById(Integer id);

    public void deleteUser(User user);
}