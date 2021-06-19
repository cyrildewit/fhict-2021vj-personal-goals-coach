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
    private static final String INSERT_COACHING_STYLE_PREFERENCE = "INSERT INTO coaching_style_preferences (uuid, suggest_delete_goal_before_period, suggest_pin_goal_based_on_activity_before_period, suggest_delete_subgoal_after_last_activity_before_period, suggest_create_subgoal_for_subgoal_activity_before_period, goal_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
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
            preparedStatement.setLong(2, coachingStylePreference.getSuggestDeleteGoalBeforePeriod());
            preparedStatement.setLong(3, coachingStylePreference.getSuggestPinGoalBasedOnActivityBeforePeriod());
            preparedStatement.setLong(4, coachingStylePreference.getSuggestDeleteSubgoalAfterLastActivityBeforePeriod());
            preparedStatement.setLong(5, coachingStylePreference.getSuggestCreateSubgoalForSubgoalAfterLastActivityBeforePeriod());
            preparedStatement.setLong(6, coachingStylePreference.getGoalId());
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

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
        long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        long suggestDeleteGoalBeforePeriod = result.getLong("suggest_delete_goal_before_period");
        long suggestPinGoalBasedOnActivityBeforePeriod = result.getLong("suggest_pin_goal_based_on_activity_before_period");
        long suggestDeleteSubgoalAfterLastActivityBeforePeriod = result.getLong("suggest_delete_subgoal_after_last_activity_before_period");
        long suggestCreateSubgoalAfterLastActivityBeforePeriod = result.getLong("suggest_create_subgoal_for_subgoal_activity_before_period");
        long goalId = result.getLong("goal_id");
        String createdAtString = result.getString("created_at");
        LocalDateTime createdAt = LocalDateTime.parse(createdAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        String updatedAtString = result.getString("updated_at");
        LocalDateTime updatedAt = LocalDateTime.parse(updatedAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());

        return new CoachingStylePreference(
                id,
                uuid,
                suggestDeleteGoalBeforePeriod,
                suggestPinGoalBasedOnActivityBeforePeriod,
                suggestDeleteSubgoalAfterLastActivityBeforePeriod,
                suggestCreateSubgoalAfterLastActivityBeforePeriod,
                goalId,
                createdAt,
                updatedAt
        );
    }

    protected LoggerInterface getLogger() {
        return logger;
    }
}