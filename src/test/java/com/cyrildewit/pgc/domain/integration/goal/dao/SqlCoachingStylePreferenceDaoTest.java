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

import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.activity.dao.SqlActivityDao;

import com.cyrildewit.pgc.domain.goal.dao.GoalDao;
import com.cyrildewit.pgc.domain.goal.dao.SqlCoachingStylePreferenceDao;
import com.cyrildewit.pgc.data.sql.MariaDBDataStore;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class SqlCoachingStylePreferenceDaoTest {
    private SqlCoachingStylePreferenceDao coachingStylePreferenceDao;
    private MariaDBDataStore mariaDBDataStore;
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    public SqlCoachingStylePreferenceDaoTest(
            SqlCoachingStylePreferenceDao coachingStylePreferenceDao,
            MariaDBDataStore mariaDBDataStore,
            DateTimeFormatters dateTimeFormatters
    ) {
        this.coachingStylePreferenceDao = coachingStylePreferenceDao;
        this.mariaDBDataStore = mariaDBDataStore;
        this.dateTimeFormatters = dateTimeFormatters;
    }

    @Test
    void findCoachingSytlePreferenceByGoal() {
        UUID uuid = UUID.randomUUID();

        try (Connection connection = mariaDBDataStore.getConnection();
             Statement stmt = connection.createStatement();) {

            stmt.execute("TRUNCATE TABLE coaching_style_preferences;");
            stmt.execute("INSERT INTO coaching_style_preferences (uuid, suggest_delete_goal_before_period, goal_id, created_at, updated_at) VALUES ('" + uuid + "', 3600, 1, '2021-05-10 20:15:25', '2021-05-10 20:15:25');");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Goal goal = new Goal();
        goal.setId(1L);

        Optional<CoachingStylePreference> coachingStylePreferenceOptional = coachingStylePreferenceDao.findCoachingSytlePreferenceByGoal(goal);

        assertTrue(coachingStylePreferenceOptional.isPresent());
        assertEquals(uuid, coachingStylePreferenceOptional.get().getUuid());
    }
}
