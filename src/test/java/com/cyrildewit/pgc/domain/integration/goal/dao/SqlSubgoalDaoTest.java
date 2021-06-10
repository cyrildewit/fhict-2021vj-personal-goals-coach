package com.cyrildewit.pgc.domain.goal.dao;

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

import com.cyrildewit.pgc.data.sql.MariaDBDataStore;

import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.goal.model.Subgoal;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.dao.SubgoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SqlSubgoalDao;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SqlSubgoalDaoTest {
    private SqlSubgoalDao subgoalDao;
    private MariaDBDataStore mariaDBDataStore;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public SqlSubgoalDaoTest(SqlSubgoalDao subgoalDao, MariaDBDataStore mariaDBDataStore, DateTimeFormatters dateTimeFormatters) {
        this.subgoalDao = subgoalDao;
        this.mariaDBDataStore = mariaDBDataStore;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void selectAllSubgoals() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal 1', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal 2', 'Goal description', '2021-05-10 20:15:25', 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Subgoal> subgoals = subgoalDao.selectAllSubgoals();

        assertEquals(2, subgoals.size());
    }

    @Test
    void selectAllSubgoalsForGoal() {
        // Database seeding
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 1, 2, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, 3, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Goal goal = new Goal();
        goal.setId(1);

        List<Subgoal> subgoals = subgoalDao.selectAllSubgoalsForGoal(goal);

        assertEquals(3, subgoals.size());
    }

    @Test
    void selectAllFirstLevelSubgoalsForGoal() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 1, 2, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, 3, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Goal goal = new Goal();
        goal.setId(1);

        List<Subgoal> subgoals = subgoalDao.selectAllFirstLevelSubgoalsForGoal(goal);

        assertEquals(1, subgoals.size());
    }

    @Test
    void selectAllSubgoalsForSubgoal() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, 3, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Subgoal subgoal = new Subgoal();
        subgoal.setId(1);

        List<Subgoal> subgoals = subgoalDao.selectAllSubgoalsForSubgoal(subgoal);

        assertEquals(2, subgoals.size());
    }


    @Test
    void findGoalById() {
        UUID uuid = UUID.randomUUID();
        long expectedId = -1;
        final String SETUP_SUBGOALS_TABLE_SQL = "INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (?, 'Test Subgoal 1', 'Subgoal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_SUBGOALS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE subgoals;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_SUBGOAL_ID_SQL = "SELECT id FROM subgoals WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBGOAL_ID_SQL);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                expectedId = result.getLong("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertNotEquals(-1, expectedId);

        Optional<Subgoal> optionalSubgoal = subgoalDao.findSubgoalById(expectedId);
        assertFalse(optionalSubgoal.isEmpty());

        assertEquals("Test Subgoal 1", optionalSubgoal.get().getTitle());
    }

    @Test
    void findSubgoalByUuid() {
        UUID uuid = UUID.randomUUID();
        final String SETUP_SUBGOALS_TABLE_SQL = "INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (?, 'Test Subgoal 1', 'Subgoal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_SUBGOALS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE subgoals;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Optional<Subgoal> optionalSubgoal = subgoalDao.findSubgoalByUuid(uuid);
        assertFalse(optionalSubgoal.isEmpty());

        assertEquals("Test Subgoal 1", optionalSubgoal.get().getTitle());
    }

    @Test
    void insertSubgoal() {
        Subgoal subgoal = new Subgoal(UUID.randomUUID(), "Subgoal Title 1", "Description here.", LocalDateTime.now(), 1L, 0L);
        subgoalDao.insertSubgoal(subgoal);

        String databaseSubgoalTitle = "";
        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT title FROM subgoals WHERE uuid = ?");) {
            preparedStatement.setString(1, subgoal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseSubgoalTitle = result.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(subgoal.getTitle(), databaseSubgoalTitle);
    }

    @Test
    void updateSubgoal() {
        Subgoal subgoal = new Subgoal(UUID.randomUUID(), "Subgoal Title 1", "Description here.", LocalDateTime.now(), 1L, 0L);
        String INSERT_SUBGOAL_SQL = "INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBGOAL_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE goals;");

            preparedStatement.setString(1, subgoal.getUuid().toString());
            preparedStatement.setString(2, subgoal.getTitle());
            preparedStatement.setString(3, subgoal.getDescription());
            preparedStatement.setString(4, subgoal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, subgoal.getGoalId());
            preparedStatement.setLong(6, subgoal.getParentSubgoalId());
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_SUBGOAL_ID_SQL = "SELECT id FROM subgoals WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBGOAL_ID_SQL);) {
            preparedStatement.setString(1, subgoal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                subgoal.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        subgoal.setTitle("New Updated Title");

        boolean updated = subgoalDao.updateSubgoal(subgoal);
        assertTrue(updated);

        String databaseSubgoalTitle = "";
        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT title FROM subgoals WHERE uuid = ?");) {
            preparedStatement.setString(1, subgoal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseSubgoalTitle = result.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(subgoal.getTitle(), databaseSubgoalTitle);
    }

    @Test
    void deleteSubgoalById() {
        Subgoal subgoal = new Subgoal(UUID.randomUUID(), "Subgoal Title 1", "Description here.", LocalDateTime.now(), 1L, 0L);
        String INSERT_SUBGOAL_SQL = "INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBGOAL_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE subgoals;");

            preparedStatement.setString(1, subgoal.getUuid().toString());
            preparedStatement.setString(2, subgoal.getTitle());
            preparedStatement.setString(3, subgoal.getDescription());
            preparedStatement.setString(4, subgoal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, subgoal.getGoalId());
            preparedStatement.setLong(6, subgoal.getParentSubgoalId());
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_SUBGOAL_ID_SQL = "SELECT id FROM subgoals WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBGOAL_ID_SQL);) {
            preparedStatement.setString(1, subgoal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                subgoal.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        subgoalDao.deleteSubgoalById(subgoal.getId());

        String GET_SUBGOALS_COUNT = "SELECT count(*) AS subgoals_count FROM subgoals;";
        long subgoalsCount = -1;

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBGOALS_COUNT);) {
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                subgoalsCount = result.getLong("subgoals_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(0, subgoalsCount);
    }

    @Test
    void deleteSubgoal() {
        Subgoal subgoal = new Subgoal(UUID.randomUUID(), "Subgoal Title 1", "Description here.", LocalDateTime.now(), 1L, 0L);
        String INSERT_SUBGOAL_SQL = "INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBGOAL_SQL);
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE subgoals;");

            preparedStatement.setString(1, subgoal.getUuid().toString());
            preparedStatement.setString(2, subgoal.getTitle());
            preparedStatement.setString(3, subgoal.getDescription());
            preparedStatement.setString(4, subgoal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, subgoal.getGoalId());
            preparedStatement.setLong(6, subgoal.getParentSubgoalId());
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        final String GET_GOAL_ID_SQL = "SELECT id FROM goals WHERE uuid = ?;";

        final String GET_SUBGOAL_ID_SQL = "SELECT id FROM subgoals WHERE uuid = ?;";

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBGOAL_ID_SQL);) {
            preparedStatement.setString(1, subgoal.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                subgoal.setId(result.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        subgoalDao.deleteSubgoal(subgoal);

        String GET_SUBGOALS_COUNT = "SELECT count(*) AS subgoals_count FROM subgoals;";
        long subgoalsCount = -1;

        try (Connection connection = mariaDBDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_SUBGOALS_COUNT);) {
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                subgoalsCount = result.getLong("subgoals_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(0, subgoalsCount);
    }

    @Test
    void countAllSubgoalsForGoal() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, 3, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Goal goal = new Goal();
        goal.setId(1L);

        assertEquals(3, subgoalDao.countAllSubgoalsForGoal(goal));
    }

    @Test
    void countAllFistLevelSubgoalsForGoal() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, 3, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Goal goal = new Goal();
        goal.setId(1L);

        assertEquals(1, subgoalDao.countAllFistLevelSubgoalsForGoal(goal));
    }

    @Test
    void countAllSubgoalsForSubgoal() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES ('c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, 3, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Subgoal subgoal = new Subgoal();
        subgoal.setId(1L);

        assertEquals(2, subgoalDao.countAllSubgoalsForSubgoal(subgoal));
    }

    @Test
    void countAllFistLevelSubgoalsForSubgoal() {
        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE subgoals;");
            stmt.execute("INSERT INTO subgoals (id, uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (1, '16bc6513-18df-4530-ab20-cb5e76412544', 'Test Goal', 'Goal description 1', '2021-05-10 20:15:25', 1, null, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (id, uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (2, 'c38aeed1-9077-4194-9d6c-fe0fc3f12345', 'Test Goal', 'Goal description 2', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (id, uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (3, 'de18094e-d845-488b-801f-041295e1febe', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 1, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO subgoals (id, uuid, title, description, deadline, goal_id, parent_subgoal_id, created_at, updated_at) VALUES (4, 'b047d697-fcfb-4ba2-96ec-dde84dcdf15b', 'Test Goal', 'Goal description 3', '2021-05-10 20:15:25', 2, 3, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Subgoal subgoal = new Subgoal();
        subgoal.setId(1L);

        assertEquals(2, subgoalDao.countAllFistLevelSubgoalsForSubgoal(subgoal));
    }
}
