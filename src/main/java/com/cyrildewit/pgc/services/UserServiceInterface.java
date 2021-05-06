package com.cyrildewit.pgc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.dao.UserDao;

public interface UserServiceInterface
{
    public List<User> getAllUsers();

    public Optional<User> findUserById(Long id);

    public Optional<User> findUserByUuid(UUID uuid);

    public void addUser(User user);

    public void updateUser(User user, String[] params);

    public void deleteUserById(Long id);

    public void deleteUser(User user);
}
