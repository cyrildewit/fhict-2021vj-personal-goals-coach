package com.cyrildewit.pgc.model;

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

import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.dao.GoalDao;
import com.cyrildewit.pgc.dao.SqlGoalDao;
import com.cyrildewit.pgc.datasource.MariaDBDriver;
import com.cyrildewit.pgc.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SqlGoalDaoTest {
    private SqlGoalDao goalDao;
    private MariaDBDriver mariaDBDriver;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public SqlGoalDaoTest(SqlGoalDao goalDao, MariaDBDriver mariaDBDriver, DateTimeFormatters dateTimeFormatters) {
        this.goalDao = goalDao;
        this.mariaDBDriver = mariaDBDriver;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void selectAllGoals() {
        // Database seeding
//        final String SQL_STATEMENTS = "TRUNCATE TABLE goals;" +
//                "INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal 1', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');" +
//                "INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal 2', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');";
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE goals;");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal 1', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal 2', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        List<Goal> goals = goalDao.selectAllGoals();

        assertEquals(2, goals.size());
    }

    @Test
    void selectAllGoalsForUser() {
        // Database seeding
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE goals;");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        User user = new User();
        user.setId(1);

        List<Goal> goals = goalDao.selectAllGoalsForUser(user);

        assertEquals(2, goals.size());
    }

    @Test
    void findGoalById() {
        UUID uuid = UUID.randomUUID();
        long expectedId = 0;
        final String SETUP_GOALS_TABLE_SQL = "INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES (?, 'Test Goal 1', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_GOALS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE goals;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        final String GET_GOAL_ID_SQL = "SELECT id FROM goals WHERE uuid = ?;";

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_GOAL_ID_SQL);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                expectedId = result.getLong("id");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        assertNotEquals(0, expectedId);

        Optional<Goal> optionalGoal = goalDao.findGoalById(expectedId);
        assertFalse(optionalGoal.isEmpty());

        assertEquals("Test Goal 1", optionalGoal.get().getTitle());
    }

    @Test
    void findGoalByUuid() {
        UUID uuid = UUID.randomUUID();
        final String SETUP_GOALS_TABLE_SQL = "INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES (?, 'Test Goal 1', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_GOALS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE goals;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        Optional<Goal> optionalGoal = goalDao.findGoalByUuid(uuid);
        assertFalse(optionalGoal.isEmpty());

        assertEquals("Test Goal 1", optionalGoal.get().getTitle());
    }

    @Test
    void insertGoal() {
        Goal goal = new Goal(UUID.randomUUID(), "Goal Title 1", "Description here.", LocalDateTime.now(), 1L);
        goalDao.insertGoal(goal);

        String databaseGoalTitle = "";
        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT title FROM goals WHERE uuid = ?");) {
            preparedStatement.setString(1, goal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseGoalTitle = result.getString("title");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        assertEquals(goal.getTitle(), databaseGoalTitle);
    }

    @Test
    void updateGoal() {
        Goal goal = new Goal(UUID.randomUUID(), "Goal Title 1", "Description here.", LocalDateTime.now(), 1L);
        String INSERT_GOAL_SQL = "INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GOAL_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE goals;");

            preparedStatement.setString(1, goal.getUuid().toString());
            preparedStatement.setString(2, goal.getTitle());
            preparedStatement.setString(3, goal.getDescription());
            preparedStatement.setString(4, goal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, goal.getUserId());
            preparedStatement.setString(6, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        final String GET_GOAL_ID_SQL = "SELECT id FROM goals WHERE uuid = ?;";

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_GOAL_ID_SQL);) {
            preparedStatement.setString(1, goal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                goal.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        goal.setTitle("New Updated Title");

        boolean updated = goalDao.updateGoal(goal);
        assertTrue(updated);

        String databaseGoalTitle = "";
        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT title FROM goals WHERE uuid = ?");) {
            preparedStatement.setString(1, goal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseGoalTitle = result.getString("title");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        assertEquals(goal.getTitle(), databaseGoalTitle);
    }

    @Test
    void deleteGoalById() {
        Goal goal = new Goal(UUID.randomUUID(), "Goal Title 1", "Description here.", LocalDateTime.now(), 1L);
        String INSERT_GOAL_SQL = "INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GOAL_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE goals;");

            preparedStatement.setString(1, goal.getUuid().toString());
            preparedStatement.setString(2, goal.getTitle());
            preparedStatement.setString(3, goal.getDescription());
            preparedStatement.setString(4, goal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, goal.getUserId());
            preparedStatement.setString(6, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        final String GET_GOAL_ID_SQL = "SELECT id FROM goals WHERE uuid = ?;";

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_GOAL_ID_SQL);) {
            preparedStatement.setString(1, goal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                goal.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        goalDao.deleteGoalById(goal.getId());

        String GET_GOALS_COUNT = "SELECT count(*) AS goals_count FROM goals;";
        long goalsCount = -1;

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_GOALS_COUNT);) {
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                goalsCount = result.getLong("goals_count");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        assertEquals(0, goalsCount);
    }

    @Test
    void getTotalGoalsCountForUser() {
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE goals;");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        User user = new User();
        user.setId(1L);

        assertEquals(2, goalDao.getTotalGoalsCountForUser(user));
    }
}
