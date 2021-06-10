package com.cyrildewit.pgc.domain.user.dao;

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
import com.cyrildewit.pgc.domain.user.dao.SqlUserDao;
import com.cyrildewit.pgc.data.sql.MariaDBDataStore;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SqlUserDaoTest {
    private SqlUserDao userDao;
    private MariaDBDataStore mariaDBDataStore;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public SqlUserDaoTest(
            SqlUserDao userDao,
            MariaDBDataStore mariaDBDataStore,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.userDao = userDao;
        this.mariaDBDataStore = mariaDBDataStore;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void selectAllUsers() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE users;");
            stmt.execute("INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'John', 'Doe', '067883838', 'john@example.com', '2021-05-10 20:15:25', 'password', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Jane', 'Doe', '067883838', 'jane@example.com', '2021-05-10 20:15:25', 'password', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<User> users = userDao.selectAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void findUserById() {
        UUID uuid = UUID.randomUUID();
        long expectedId = 0;
        final String SETUP_USERS_TABLE_SQL = "INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES (?, 'John', 'Doe', '067883838', 'john@example.com', '2021-05-10 20:15:25', 'password', '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_USERS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE users;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_USER_ID_SQL = "SELECT id FROM users WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ID_SQL);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                expectedId = result.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotEquals(0, expectedId);

        Optional<User> optionalUser = userDao.findUserById(expectedId);
        assertFalse(optionalUser.isEmpty());

        assertEquals("John", optionalUser.get().getFirstName());
    }

    @Test
    void findUserByUuid() {
        UUID uuid = UUID.randomUUID();
        final String SETUP_USERS_TABLE_SQL = "INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES (?, 'John', 'Doe', '067883838', 'john@example.com', '2021-05-10 20:15:25', 'password', '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_USERS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE users;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Optional<User> optionalUser = userDao.findUserByUuid(uuid);
        assertFalse(optionalUser.isEmpty());

        assertEquals("John", optionalUser.get().getFirstName());
    }

    @Test
    void findUserByEmail() {
        UUID uuid = UUID.randomUUID();
        final String SETUP_USERS_TABLE_SQL = "INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES (?, 'John', 'Doe', '067883838', 'john@example.com', '2021-05-10 20:15:25', 'password', '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_USERS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE users;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Optional<User> optionalUser = userDao.findUserByEmail("john@example.com");
        assertFalse(optionalUser.isEmpty());

        assertEquals("John", optionalUser.get().getFirstName());
    }

    @Test
    void insertUser() {
        final String testEmail = "john@example.com";

        User user = new User(UUID.randomUUID(), "John", "Doe", "0628282828", testEmail, LocalDateTime.now(), "password");
        userDao.insertUser(user);

        String databaseUserFirstName = "";
        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name FROM users WHERE email = ?");) {
            preparedStatement.setString(1, testEmail);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseUserFirstName = result.getString("first_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(user.getFirstName(), databaseUserFirstName);
    }

    @Test
    void updateUser() {
        User user = new User(UUID.randomUUID(), "John", "Doe", "0628282828", "john@example.com", LocalDateTime.now(), "password");
        String INSERT_USER_SQL = "INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE users;");

            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getEmailVerifiedAt().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(9, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_USER_ID_SQL = "SELECT id FROM users WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ID_SQL);) {
            preparedStatement.setString(1, user.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                user.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        user.setFirstName("Changed Name");

        boolean updated = userDao.updateUser(user);
        assertTrue(updated);

        String databaseUserFirstName = "";
        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT first_name FROM users WHERE uuid = ?");) {
            preparedStatement.setString(1, user.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseUserFirstName = result.getString("first_name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(user.getFirstName(), databaseUserFirstName);
    }

    @Test
    void deleteUserById() {
        User user = new User(UUID.randomUUID(), "John", "Doe", "0628282828", "john@example.com", LocalDateTime.now(), "password");
        String INSERT_USER_SQL = "INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE users;");

            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getEmailVerifiedAt().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(9, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_USERL_ID_SQL = "SELECT id FROM users WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USERL_ID_SQL);) {
            preparedStatement.setString(1, user.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                user.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userDao.deleteUserById(user.getId());

        String GET_USERS_COUNT = "SELECT count(*) AS users_count FROM users;";
        long usersCount = -1;

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS_COUNT);) {
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                usersCount = result.getLong("users_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(0, usersCount);
    }

    @Test
    void deleteUser() {
        User user = new User(UUID.randomUUID(), "John", "Doe", "0628282828", "john@example.com", LocalDateTime.now(), "password");
        String INSERT_USER_SQL = "INSERT INTO users (uuid, first_name, last_name, phone_number, email, email_verified_at, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE users;");

            preparedStatement.setString(1, user.getUuid().toString());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPhoneNumber());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getEmailVerifiedAt().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(9, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_USER_ID_SQL = "SELECT id FROM users WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ID_SQL);) {
            preparedStatement.setString(1, user.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                user.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        userDao.deleteUser(user);

        String GET_USERS_COUNT = "SELECT count(*) AS users_count FROM users;";
        long usersCount = -1;

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_USERS_COUNT);) {
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                usersCount = result.getLong("users_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(0, usersCount);
    }
}
