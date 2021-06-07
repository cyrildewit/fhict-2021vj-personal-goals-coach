package com.cyrildewit.pgc.logic.model;

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

import com.cyrildewit.pgc.logic.model.User;
import com.cyrildewit.pgc.logic.model.Goal;
import com.cyrildewit.pgc.dao.GoalDao;
import com.cyrildewit.pgc.dao.SqlActivityDao;
import com.cyrildewit.pgc.datasource.MariaDBDriver;
import com.cyrildewit.pgc.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SqlActivityDaoTest {
    private SqlActivityDao activityDao;
    private MariaDBDriver mariaDBDriver;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public SqlActivityDaoTest(SqlActivityDao activityDao, MariaDBDriver mariaDBDriver, DateTimeFormatters dateTimeFormatters) {
        this.activityDao = activityDao;
        this.mariaDBDriver = mariaDBDriver;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void selectAllActvityForSubject() {
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE activities;");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 1, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 1, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 2, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        Goal goal = new Goal();
        goal.setId(1L);

        List<Activity> activities = activityDao.selectAllActvityForSubject(goal);

        assertEquals(2, activities.size());
    }

    @Test
    void selectActvityWithinPeriodForSubjectAndCauser() {
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE activities;");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 1, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 1, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 1, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 2, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        Goal goal = new Goal();
        goal.setId(1L);

        User user = new User();
        user.setId(1L);

        List<Activity> activities = activityDao.selectActvityWithinPeriodForSubjectAndCauser(goal, user, LocalDateTime.now().minusYears(1), LocalDateTime.now());

        assertEquals(2, activities.size());
    }

    @Test
    void selectLatestActivityForSubject() {
        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE activities;");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 1, 'Goal', 1, 'User', '2021-01-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description of latest activity', 1, 'Goal', 1, 'User', '2021-02-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 1, 'User', '2021-03-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 1, 'User', '2021-04-10 20:15:25', '2021-05-10 20:15:25');");
            stmt.execute("INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES ('"+ UUID.randomUUID()+"', '', 'description', 2, 'Goal', 2, 'User', '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        Goal goal = new Goal();
        goal.setId(1L);

        Optional<Activity> optionalActivity = activityDao.selectLatestActivityForSubject(goal);
        assertFalse(optionalActivity.isEmpty());

        assertEquals("description of latest activity", optionalActivity.get().getDescription());
    }

    @Test
    void insertActivity() {
        Activity activity = new Activity(UUID.randomUUID(), "", "The most important description.", 1, "Goal", 1, "User");
        activityDao.insertActivity(activity);

        String databaseActivityDescription = "";
        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT description FROM activities WHERE uuid = ?");) {
            preparedStatement.setString(1, activity.getUuid().toString());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                databaseActivityDescription = result.getString("description");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        assertEquals(activity.getDescription(), databaseActivityDescription);
    }
}
