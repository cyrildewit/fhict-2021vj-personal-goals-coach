package com.cyrildewit.pgc.application.feature.services;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.Optional;
import java.time.LocalDateTime;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.application.services.AuthenticationService;
import com.cyrildewit.pgc.data.sql.MariaDBDataStore;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

import com.cyrildewit.pgc.application.exceptions.NotAuthenticatedException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AuthenticationServiceTest {
    private AuthenticationService authenticationService;
    private MariaDBDataStore mariaDBDataStore;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public AuthenticationServiceTest(
            AuthenticationService authenticationService,
            MariaDBDataStore mariaDBDataStore,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.authenticationService = authenticationService;
        this.mariaDBDataStore = mariaDBDataStore;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void attemptLoginFailsIfNoUserIsFoundWithGivenEmailAddress() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE users;");
            stmt.execute("INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'John', 'Doe', '0628838283', '" + testEmail + "', '2021-05-10 22:22:45', '" + testPassword + "', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertFalse(authenticationService.attemptLogin("wrong-email@example.com", testPassword));
    }

    @Test
    void attemptLoginFailsIfPasswordIsNotCorrect() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE users;");
            stmt.execute("INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'John', 'Doe', '0628838283', '" + testEmail + "', '2021-05-10 22:22:45', '" + testPassword + "', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertFalse(authenticationService.attemptLogin(testEmail, "wrong password"));
    }

    @Test
    void attemptLoginSucceedsIfPasswordIsCorrect() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE users;");
            stmt.execute("INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'John', 'Doe', '0628838283', '" + testEmail + "', '2021-05-10 22:22:45', '" + testPassword + "', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertFalse(authenticationService.attemptLogin(testEmail, testPassword));
    }

    @Test
    void afterALoginAttemptSucceedsTheCurrentUserWillBeUpdated() {
        String testEmail = "test@example.com";
        String testPassword = "password";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE users;");
            stmt.execute("INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'John', 'Doe', '0628838283', '" + testEmail + "', '2021-05-10 22:22:45', '" + testPassword + "', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertTrue(authenticationService.attemptLogin(testEmail, testPassword));
        assertEquals("John", authenticationService.getCurrentUser().getFirstName());
    }
}
