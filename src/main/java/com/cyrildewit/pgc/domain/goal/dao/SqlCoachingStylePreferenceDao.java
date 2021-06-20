package com.cyrildewit.pgc.domain.goal.dao;

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

import com.cyrildewit.pgc.data.sql.SqlDataStore;
import com.cyrildewit.pgc.data.sql.MariaDBDataStore;

import com.cyrildewit.pgc.domain.BaseDao;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.goal.dao.CoachingStylePreferenceDao;

import com.cyrildewit.pgc.support.logging.LoggerInterface;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@Component
public class SqlCoachingStylePreferenceDao extends BaseDao implements CoachingStylePreferenceDao {
    private DateTimeFormatters dateTimeFormatters;
    private SqlDataStore sqlDataStore;
    private LoggerInterface logger;

    public SqlCoachingStylePreferenceDao(
            DateTimeFormatters dateTimeFormatters,
            MariaDBDataStore mariaDBDataStore,
            LoggerInterface logger
    ) {
        this.dateTimeFormatters = dateTimeFormatters;
        this.sqlDataStore = mariaDBDataStore;
        this.logger = logger;
    }
    private static final String FIND_COACHING_STYLE_PREFERENCE_BY_GOAL = "SELECT * from coaching_style_preferences WHERE goal_id = ? LIMIT 1";
    private static final String INSERT_COACHING_STYLE_PREFERENCE = "INSERT INTO coaching_style_preferences (uuid, is_suggest_delete_goal_enabled, suggest_delete_goal_before_period, is_suggest_pin_goal_enabled, suggest_pin_goal_based_on_activity_before_period, is_suggest_delete_subgoal_enabled, suggest_delete_subgoal_after_last_activity_before_period, is_suggest_create_subgoal_for_subgoal_enabled, suggest_create_subgoal_for_subgoal_activity_before_period, goal_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String TRUNCATE_TABLE = "TRUNCATE coaching_style_preferences";

    public Optional<CoachingStylePreference> findCoachingSytlePreferenceByGoal(Goal goal) {
        Optional<CoachingStylePreference> coachingStylePreferenceOptional = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(FIND_COACHING_STYLE_PREFERENCE_BY_GOAL);) {
            preparedStatement.setLong(1, goal.getId());
            ResultSet result = preparedStatement.executeQuery();

            coachingStylePreferenceOptional = resolveFirstCoachingStylePreferenceFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return coachingStylePreferenceOptional;
    }

    public void truncate() {
        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TRUNCATE_TABLE);) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    private Optional<CoachingStylePreference> resolveFirstCoachingStylePreferenceFromResultSet(ResultSet result) throws SQLException {
        Optional<CoachingStylePreference> coachingStylePreferenceOptional = Optional.empty();

        while (result.next()) {
            coachingStylePreferenceOptional = Optional.of(mapResultSetToCoachingStylePreference(result));
        }

        return coachingStylePreferenceOptional;
    }

    public void insertCoachingStylePreference(CoachingStylePreference coachingStylePreference) {
        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COACHING_STYLE_PREFERENCE);) {
            preparedStatement.setString(1, coachingStylePreference.getUuid().toString());
            preparedStatement.setBoolean(2, coachingStylePreference.isSuggestDeleteGoalEnabled());
            preparedStatement.setLong(3, coachingStylePreference.getSuggestDeleteGoalBeforePeriod());
            preparedStatement.setBoolean(4, coachingStylePreference.isSuggestPinGoalEnabled());
            preparedStatement.setLong(5, coachingStylePreference.getSuggestPinGoalBasedOnActivityBeforePeriod());
            preparedStatement.setBoolean(6, coachingStylePreference.isSuggestDeleteSubgoalEnabled());
            preparedStatement.setLong(7, coachingStylePreference.getSuggestDeleteSubgoalAfterLastActivityBeforePeriod());
            preparedStatement.setBoolean(8, coachingStylePreference.isSuggestCreateSubgoalForSubgoalEnabled());
            preparedStatement.setLong(9, coachingStylePreference.getSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod());
            preparedStatement.setLong(10, coachingStylePreference.getGoalId());
            preparedStatement.setString(11, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(12, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    public void insertGoal(Goal goal) {

    }

    private List<CoachingStylePreference> resolveCoachingStylePreferencesFromResultSet(ResultSet result) throws SQLException {
        List<CoachingStylePreference> coachingStylePreferences = new ArrayList<CoachingStylePreference>();

        while (result.next()) {
            coachingStylePreferences.add(mapResultSetToCoachingStylePreference(result));
        }

        return coachingStylePreferences;
    }

    private CoachingStylePreference mapResultSetToCoachingStylePreference(ResultSet result) throws SQLException {
        return new CoachingStylePreference(
                result.getLong("id"),
                UUID.fromString(result.getString("uuid")),
                result.getBoolean("is_suggest_delete_goal_enabled"),
                result.getLong("suggest_delete_goal_before_period"),
                result.getBoolean("is_suggest_pin_goal_enabled"),
                result.getLong("suggest_pin_goal_based_on_activity_before_period"),
                result.getBoolean("is_suggest_delete_subgoal_enabled"),
                result.getLong("suggest_delete_subgoal_after_last_activity_before_period"),
                result.getBoolean("is_suggest_create_subgoal_enabled"),
                result.getLong("suggest_create_subgoal_after_last_activity_before_period"),
                result.getBoolean("is_suggest_create_subgoal_for_subgoal_enabled"),
                result.getLong("suggest_create_subgoal_for_subgoal_activity_before_period"),
                result.getLong("goal_id"),
                LocalDateTime.parse(result.getString("created_at"), dateTimeFormatters.getMariaDbDateTimeFormatter()),
                LocalDateTime.parse(result.getString("updated_at"), dateTimeFormatters.getMariaDbDateTimeFormatter())
        );
    }

    protected LoggerInterface getLogger() {
        return logger;
    }
}