package com.cyrildewit.pgc.domain.user.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.data.sql.SqlDataStore;
import com.cyrildewit.pgc.data.sql.MariaDBDataStore;

import com.cyrildewit.pgc.domain.BaseDao;
import com.cyrildewit.pgc.domain.user.model.User;

import com.cyrildewit.pgc.support.logging.LoggerInterface;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@Component
public class SqlUserDao extends BaseDao implements UserDao {
    private DateTimeFormatters dateTimeFormatters;
    private SqlDataStore sqlDataStore;
    private LoggerInterface logger;

    public SqlUserDao(
            DateTimeFormatters dateTimeFormatters,
            MariaDBDataStore mariaDBDataStore,
            LoggerInterface logger
    ) {
        this.dateTimeFormatters = dateTimeFormatters;
        this.sqlDataStore = mariaDBDataStore;
        this.logger = logger;
    }

    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?;";
    private static final String SELECT_USER_BY_UUID = "SELECT * FROM users WHERE uuid = ?;";
    private static final String SELECT_USER_BY_EMAIL = "SELECT * FROM users WHERE email = ?;";
    private static final String UPDATE_USER = "UPDATE users SET first_name = ?, last_name = ?, phone_number = ?, email = ?, email_verified_at = ?, password = ?, updated_at = ? WHERE id = ?;";
    private static final String INSERT_USER = "INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE id = ?;";

    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<User>();

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
             ResultSet result = preparedStatement.executeQuery();

            users = resolveUsersFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return users;
    }

    public Optional<User> findUserById(long id) {
        Optional<User> user = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            user = resolveFirstUserFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return user;
    }

    public Optional<User> findUserByUuid(UUID uuid) {
        Optional<User> user = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_UUID);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            user = resolveFirstUserFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return user;
    }

    public Optional<User> findUserByEmail(String email) {
        Optional<User> user = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_EMAIL);) {
            preparedStatement.setString(1, email);
            ResultSet result = preparedStatement.executeQuery();

            user = resolveFirstUserFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return user;
    }

    public void insertUser(User user) {
        try (Connection connection = sqlDataStore.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER);) {
            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getEmailVerifiedAt().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setString(6, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(6, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    public boolean updateUser(User user) {
        boolean rowUpdated = false;

        try (Connection connection = sqlDataStore.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER);) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getPhoneNumber());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getEmailVerifiedAt().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(8, user.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            logSQLException(e);
        }

        return rowUpdated;
    }

    public void deleteUserById(long id) {
        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID);) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    public void deleteUser(User user) {
        deleteUserById(user.getId());
    }

    private Optional<User> resolveFirstUserFromResultSet(ResultSet result) throws SQLException {
        Optional<User> user = Optional.empty();

        while (result.next()) {
            user = Optional.of(mapResultSetToUser(result));
        }

        return user;
    }

    private List<User> resolveUsersFromResultSet(ResultSet result) throws SQLException {
        List<User> users = new ArrayList<User>();

        while (result.next()) {
            users.add(mapResultSetToUser(result));
        }

        return users;
    }

    private User mapResultSetToUser(ResultSet result) throws SQLException {
        long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        String firstName = result.getString("first_name");
        String lastName = result.getString("last_name");
        String phoneNumber = result.getString("phone_number");
        String email = result.getString("email");
        String emailVerifiedAtString = result.getString("email_verified_at");
        LocalDateTime emailVerifiedAt = LocalDateTime.parse(emailVerifiedAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        String password = result.getString("password");
        String createdAtString = result.getString("created_at");
        LocalDateTime createdAt = LocalDateTime.parse(createdAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        String updatedAtString = result.getString("updated_at");
        LocalDateTime updatedAt = LocalDateTime.parse(updatedAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());

        return new User(id, uuid, firstName, lastName, phoneNumber, email, emailVerifiedAt, password, createdAt, updatedAt);
    }

    protected LoggerInterface getLogger() {
        return logger;
    }
}