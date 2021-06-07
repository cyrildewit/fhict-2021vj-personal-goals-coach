package com.cyrildewit.pgc.domain.model;

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
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.suggestive_action.model.SuggestiveAction;
import com.cyrildewit.pgc.domain.suggestive_action.enums.SuggestiveActionType;

import com.cyrildewit.pgc.application.dao.GoalDao;
import com.cyrildewit.pgc.application.dao.SqlSuggestiveActionDao;
import com.cyrildewit.pgc.application.datasource.MariaDBDriver;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SqlSuggestiveActionDaoTest {
    private SqlSuggestiveActionDao suggestiveActionDao;
    private MariaDBDriver mariaDBDriver;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public SqlSuggestiveActionDaoTest(SqlSuggestiveActionDao suggestiveActionDao, MariaDBDriver mariaDBDriver, DateTimeFormatters dateTimeFormatters) {
        this.suggestiveActionDao = suggestiveActionDao;
        this.mariaDBDriver = mariaDBDriver;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void selectAllSuggestiveActionsForGoal() {
        // Database seeding
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE suggestive_actions;");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 0, 1, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('9ba35a87-03d8-4d27-a28b-dae02fedd231', 0, 1, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('bed59dd4-4763-451e-a66b-584144eb97eb', 0, 1, 2, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('cb8ce0ac-2e37-4825-94b7-6402ddc26677', 0, 2, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        Goal goal = new Goal();
        goal.setId(1);

        List<SuggestiveAction> suggestiveActions = suggestiveActionDao.selectAllSuggestiveActionsForGoal(goal);

        assertEquals(3, suggestiveActions.size());
    }

    @Test
    void suggestiveActionExists() {
        UUID uuid = UUID.randomUUID();

        final String SETUP_SUGGESTIVE_ACTIONS_TABLE_SQL = "INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES (?, 0, 1, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');";

        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();
             PreparedStatement preparedStatement = connection.prepareStatement(SETUP_SUGGESTIVE_ACTIONS_TABLE_SQL);) {
            stmt.execute("TRUNCATE TABLE suggestive_actions;");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        SuggestiveAction newSuggestiveAction = new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.DELETE_GOAL, 1L, 1L, 0L);

        assertTrue(suggestiveActionDao.suggestiveActionExists(newSuggestiveAction));
    }

    @Test
    void insertSuggestiveAction() {
        SuggestiveAction suggestiveAction = new SuggestiveAction(UUID.randomUUID(), SuggestiveActionType.DELETE_GOAL, 1L, 1L, 0L);
        suggestiveActionDao.insertSuggestiveAction(suggestiveAction);

        int databaseSuggestiveType = -1;
        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT type FROM suggestive_actions WHERE uuid = ?");) {
            preparedStatement.setString(1, suggestiveAction.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseSuggestiveType = result.getInt("type");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        assertEquals(0, databaseSuggestiveType);
    }

    @Test
    void getTotalSuggestiveActionsCountForUser() {
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE suggestive_actions;");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 0, 1, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('9ba35a87-03d8-4d27-a28b-dae02fedd231', 0, 1, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('bed59dd4-4763-451e-a66b-584144eb97eb', 0, 1, 2, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('cb8ce0ac-2e37-4825-94b7-6402ddc26677', 0, 2, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        User user = new User();
        user.setId(1L);

        assertEquals(3, suggestiveActionDao.getTotalSuggestiveActionsCountForUser(user));
    }

    @Test
    void getTotalSuggestiveActionsCountForGoal() {
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {
            stmt.execute("TRUNCATE TABLE suggestive_actions;");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('16bc6513-18df-4530-ab20-cb5e76412544', 0, 1, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('9ba35a87-03d8-4d27-a28b-dae02fedd231', 0, 1, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('bed59dd4-4763-451e-a66b-584144eb97eb', 0, 1, 2, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES ('cb8ce0ac-2e37-4825-94b7-6402ddc26677', 0, 2, 1, 0, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        Goal goal = new Goal();
        goal.setId(1L);

        assertEquals(3, suggestiveActionDao.getTotalSuggestiveActionsCountForGoal(goal));
    }
}
