package com.cyrildewit.pgc.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

import com.cyrildewit.pgc.model.User;

public interface UserDao {
    public List<User> selectAllUsers();

    public Optional<User> findUserById(Long id);

    public Optional<User> findUserByUuid(UUID uuid);

    public void insertUser(User user);

    public void updateUser(User user);

    public void deleteUserById(Long id);

    public void deleteUser(User user);
}