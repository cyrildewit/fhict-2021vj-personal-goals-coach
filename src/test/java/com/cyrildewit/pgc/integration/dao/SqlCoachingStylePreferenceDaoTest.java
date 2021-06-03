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
import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.dao.GoalDao;
import com.cyrildewit.pgc.dao.SqlActivityDao;
import com.cyrildewit.pgc.dao.SqlCoachingStylePreferenceDao;
import com.cyrildewit.pgc.datasource.MariaDBDriver;
import com.cyrildewit.pgc.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SqlCoachingStylePreferenceDaoTest {
    private SqlCoachingStylePreferenceDao coachingStylePreferenceDao;
    private MariaDBDriver mariaDBDriver;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public SqlCoachingStylePreferenceDaoTest(SqlCoachingStylePreferenceDao coachingStylePreferenceDao, MariaDBDriver mariaDBDriver, DateTimeFormatters dateTimeFormatters) {
        this.coachingStylePreferenceDao = coachingStylePreferenceDao;
        this.mariaDBDriver = mariaDBDriver;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void findCoachingSytlePreferenceByGoal() {
        UUID uuid = UUID.randomUUID();

        try (Connection connection = mariaDBDriver.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE coaching_style_preferences;");
            stmt.execute("INSERT INTO coaching_style_preferences (uuid, suggest_delete_goal_before_period, goal_id, created_at, updated_at) VALUES ('"+ uuid +"', 3600, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        Goal goal = new Goal();
        goal.setId(1L);

        Optional<CoachingStylePreference> coachingStylePreferenceOptional = coachingStylePreferenceDao.findCoachingSytlePreferenceByGoal(goal);

        assertTrue(coachingStylePreferenceOptional.isPresent());
        assertEquals(uuid, coachingStylePreferenceOptional.get().getUuid());
    }
}
