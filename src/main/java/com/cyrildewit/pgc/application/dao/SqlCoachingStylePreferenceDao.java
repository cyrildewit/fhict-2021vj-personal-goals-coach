package com.cyrildewit.pgc.application.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;
import com.cyrildewit.pgc.application.datasource.MariaDBDriver;
import com.cyrildewit.pgc.application.dao.CoachingStylePreferenceDao;

@Component
public class SqlCoachingStylePreferenceDao implements CoachingStylePreferenceDao {
    private DateTimeFormatters dateTimeFormatters;

    private MariaDBDriver mariaDBDriver;

    public SqlCoachingStylePreferenceDao(DateTimeFormatters dateTimeFormatters, MariaDBDriver mariaDBDriver) {
        this.dateTimeFormatters = dateTimeFormatters;
        this.mariaDBDriver = mariaDBDriver;
    }

    private static final String FIND_COACHING_STYLE_PREFERENCE_BY_GOAL = "SELECT * from coaching_style_preferences WHERE goal_id = ? LIMIT 1";

    public Optional<CoachingStylePreference> findCoachingSytlePreferenceByGoal(Goal goal) {
        Optional<CoachingStylePreference> coachingStylePreferenceOptional = Optional.empty();

        try (Connection connection = mariaDBDriver.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(FIND_COACHING_STYLE_PREFERENCE_BY_GOAL);) {
            preparedStatement.setLong(1, goal.getId());
            ResultSet result = preparedStatement.executeQuery();

            coachingStylePreferenceOptional = resolveFirstCoachingStylePreferenceFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return coachingStylePreferenceOptional;
    }

    private Optional<CoachingStylePreference> resolveFirstCoachingStylePreferenceFromResultSet(ResultSet result) throws SQLException {
        Optional<CoachingStylePreference> coachingStylePreferenceOptional = Optional.empty();

        while (result.next()) {
            coachingStylePreferenceOptional = Optional.of(mapResultSetToCoachingStylePreference(result));
        }

        return coachingStylePreferenceOptional;
    }

    private List<CoachingStylePreference> resolveCoachingStylePreferencesFromResultSet(ResultSet result) throws SQLException {
        List<CoachingStylePreference> coachingStylePreferences = new ArrayList<CoachingStylePreference>();

        while (result.next()) {
            coachingStylePreferences.add(mapResultSetToCoachingStylePreference(result));
        }

        return coachingStylePreferences;
    }

    private CoachingStylePreference mapResultSetToCoachingStylePreference(ResultSet result) throws SQLException {
        long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        long suggestDeleteGoalBeforePeriod = result.getLong("suggest_delete_goal_before_period");
        long suggestPinGoalBasedOnActivityBeforePeriod = result.getLong("suggest_pin_goal_based_on_activity_before_period");
        long goalId = result.getLong("goal_id");
        String createdAtString = result.getString("created_at");
        LocalDateTime createdAt = LocalDateTime.parse(createdAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        String updatedAtString = result.getString("updated_at");
        LocalDateTime updatedAt = LocalDateTime.parse(updatedAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());

        return new CoachingStylePreference(id, uuid, suggestDeleteGoalBeforePeriod, suggestPinGoalBasedOnActivityBeforePeriod, goalId, createdAt, updatedAt);
    }
}