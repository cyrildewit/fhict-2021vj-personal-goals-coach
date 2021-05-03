package com.cyrildewit.pgc.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.models.User;
import com.cyrildewit.pgc.dao.UserDao;
import com.cyrildewit.pgc.dao.InMemoryUserDao;

@Service
public class UserService
{
    private final UserDao userDao;

    @Autowired
    public UserService(InMemoryUserDao userDao)
    {
        this.userDao = userDao;
    }

    public List<User> getAllUsers()
    {
        return userDao.selectAllUsers();
    }

    public Optional<User> findUserById(Integer id)
    {
        return userDao.findUserById(id);
    }

    public Optional<User> findUserByUuid(UUID uuid)
    {
        return userDao.findUserByUuid(uuid);
    }

    public void addUser(User user)
    {
        userDao.insertUser(user);
    }

    public void updateUser(User user, String[] params)
    {
        userDao.insertUser(user);
    }

    public void deleteUserById(Integer id)
    {
        userDao.deleteUserById(id);
    }

    public void deleteUser(User user)
    {
        userDao.deleteUser(user);
    }
}
