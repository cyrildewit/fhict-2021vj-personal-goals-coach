package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.application.dao.UserDao;
import com.cyrildewit.pgc.application.dao.SqlUserDao;

@Service
public class UserService
{
    private final UserDao userDao;

    @Autowired
    public UserService(SqlUserDao userDao)
    {
        this.userDao = userDao;
    }

    public List<User> getAllUsers()
    {
        return userDao.selectAllUsers();
    }

    public Optional<User> findUserById(long id)
    {
        return userDao.findUserById(id);
    }

    public Optional<User> findUserByUuid(UUID uuid)
    {
        return userDao.findUserByUuid(uuid);
    }

    public Optional<User> findUserByEmail(String email)
    {
        return userDao.findUserByEmail(email);
    }

    public void addUser(User user)
    {
        userDao.insertUser(user);
    }

    public void updateUser(User user)
    {
        userDao.insertUser(user);
    }

    public void deleteUserById(long id)
    {
        userDao.deleteUserById(id);
    }

    public void deleteUser(User user)
    {
        userDao.deleteUser(user);
    }
}
