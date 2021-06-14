package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.user.dao.UserDao;

public interface UserServiceInterface
{
    public List<User> getAllUsers();

    public Optional<User> getUserById(long id);

    public Optional<User> getUserByUuid(UUID uuid);

    public Optional<User> getUserByEmail(String email);

    public void addUser(User user);

    public void updateUser(User user);

    public void deleteUserById(long id);

    public void deleteUser(User user);
}
