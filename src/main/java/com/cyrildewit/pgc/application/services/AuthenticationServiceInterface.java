package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.user.model.User;

public interface AuthenticationServiceInterface {
    public boolean attemptLogin(String email, String password);

    public void logout();

    public User getCurrentUser();

    public boolean isAuthenticated();
}
