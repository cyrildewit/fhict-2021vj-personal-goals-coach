package com.cyrildewit.pgc.application.services;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.application.services.UserService;
import com.cyrildewit.pgc.application.exceptions.UserNotFoundException;
import com.cyrildewit.pgc.application.exceptions.NotAuthenticatedException;

@Service
public class AuthenticationService implements AuthenticationServiceInterface {
    private final UserService userService;

    private User currentUser;

    @Autowired
    public AuthenticationService(UserService userService) {
        this.userService = userService;
    }

    public void fetchFakeUser() {
        Optional<User> optionalUser = userService.findUserById(1L);
        optionalUser.orElseThrow(() -> new UserNotFoundException());
        currentUser = optionalUser.get();
    }

    /**
     * Unfinished implementation and therefore obviously not secure.
     */
    public boolean attemptLogin(String email, String password) {
        Optional<User> optionalUser = userService.findUserByEmail(email);
        if (!optionalUser.isPresent()) {
            return false;
        }
        User user = optionalUser.get();

        if (user.getPassword().trim().equals(password.trim())) {
            this.currentUser = user;

            return true;
        }

        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser()
    {
        fetchFakeUser();

        if (currentUser == null) {
            throw new NotAuthenticatedException();
        }

        return currentUser;
    }

    public boolean isAuthenticated() {
        return currentUser != null;
    }
}
