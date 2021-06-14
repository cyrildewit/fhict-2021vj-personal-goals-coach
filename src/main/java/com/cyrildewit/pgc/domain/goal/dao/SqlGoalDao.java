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
import com.cyrildewit.pgc.domain.goal.model.Goal;
import com.cyrildewit.pgc.domain.user.model.User;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.goal.model.CoachingStylePreference;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;
import com.cyrildewit.pgc.domain.goal.dao.CoachingStylePreferenceDao;

import com.cyrildewit.pgc.support.logging.LoggerInterface;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@Component
public class SqlGoalDao extends BaseDao implements GoalDao {
    private DateTimeFormatters dateTimeFormatters;
    private SqlDataStore sqlDataStore;
    private ActivityDao activityDao;
    private CoachingStylePreferenceDao coachingStylePreferenceDao;
    private LoggerInterface logger;

    public SqlGoalDao(
            DateTimeFormatters dateTimeFormatters,
            MariaDBDataStore mariaDBDataStore,
            ActivityDao activityDao,
            CoachingStylePreferenceDao coachingStylePreferenceDao,
            LoggerInterface logger
    ) {
        this.dateTimeFormatters = dateTimeFormatters;
        this.sqlDataStore = mariaDBDataStore;
        this.activityDao = activityDao;
        this.coachingStylePreferenceDao = coachingStylePreferenceDao;
        this.logger = logger;
    }

    private static final String SELECT_ALL_GOALS = "SELECT * FROM goals";
    private static final String SELECT_ALL_GOALS_FOR_USER = "SELECT * from goals WHERE user_id = ?;";
    private static final String SELECT_GOAL_BY_ID = "SELECT * FROM goals WHERE id = ?;";
    private static final String SELECT_GOAL_BY_UUID = "SELECT * FROM goals WHERE uuid = ?;";
    private static final String UPDATE_GOAL = "UPDATE goals SET title = ?, description = ?, deadline = ?, user_id = ?, updated_at = ? WHERE id = ?;";
    private static final String INSERT_GOAL = "INSERT INTO goals (uuid, title, description, deadline, user_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String DELETE_GOAL_BY_ID = "DELETE FROM goals WHERE id = ?;";
    private static final String SELECT_COUNT_GOALS_FOR_USER = "SELECT count(*) AS count FROM goals WHERE user_id = ?;";

    public List<Goal> selectAllGoals() {
        List<Goal> goals = new ArrayList<Goal>();

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GOALS);) {
            ResultSet result = preparedStatement.executeQuery();

            goals = resolveGoalsFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return loadRelationshipseForGoals(goals);
    }

    public List<Goal> selectAllGoalsForUser(User user) {
        List<Goal> goals = new ArrayList<Goal>();

        try (Connection connection = sqlDataStore.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GOALS_FOR_USER);) {
            preparedStatement.setLong(1, user.getId());
            ResultSet result = preparedStatement.executeQuery();

            goals = resolveGoalsFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return loadRelationshipseForGoals(goals);
    }

    public Optional<Goal> findGoalById(long id) {
        Optional<Goal> goal = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GOAL_BY_ID);) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            goal = resolveFirstGoalFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        if (goal.isPresent()) {
            return Optional.of(loadRelationshipseForGoal(goal.get()));
        }

        return goal;
    }

    public Optional<Goal> findGoalByUuid(UUID uuid) {
        Optional<Goal> goal = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GOAL_BY_UUID);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            goal = resolveFirstGoalFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        if (goal.isPresent()) {
            return Optional.of(loadRelationshipseForGoal(goal.get()));
        }

        return goal;
    }

    public List<Goal> findGoalsByIds(List<Long> ids) {
        String SELECT_GOAL_BY_IDS = "SELECT * FROM goals WHERE id IN (";
        SELECT_GOAL_BY_IDS += ids.toString();
        SELECT_GOAL_BY_IDS += ");";

        List<Goal> goals = new ArrayList<Goal>();

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GOAL_BY_IDS);) {
            ResultSet result = preparedStatement.executeQuery();

            goals = resolveGoalsFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return loadRelationshipseForGoals(goals);
    }

    public void insertGoal(Goal goal) {
        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GOAL);) {
            preparedStatement.setString(1, goal.getUuid().toString());
            preparedStatement.setString(2, goal.getTitle());
            preparedStatement.setString(3, goal.getDescription());
            preparedStatement.setString(4, goal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, goal.getUserId());
            preparedStatement.setString(6, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    public boolean updateGoal(Goal goal) {
        boolean rowUpdated = false;

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GOAL);) {
            preparedStatement.setString(1, goal.getTitle());
            preparedStatement.setString(2, goal.getDescription());
            preparedStatement.setString(3, goal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(4, goal.getUserId());
            preparedStatement.setString(5, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(6, goal.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            logSQLException(e);
        }

        return rowUpdated;
    }

    public void deleteGoalById(long id) {
        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_GOAL_BY_ID);) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            logSQLException(e);
        }
    }

    public void deleteGoal(Goal goal) {
        deleteGoalById(goal.getId());
    }

    public long getTotalGoalsCountForUser(User user) {
        long goalsCount = 0;

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_GOALS_FOR_USER);) {
            preparedStatement.setLong(1, user.getId());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                goalsCount = result.getLong("count");
            }
        } catch (SQLException e) {
            logSQLException(e);
        }

        return goalsCount;
    }

    public Optional<Goal> getGoalWithMostRecentActivity(LocalDateTime start, LocalDateTime end) {
        Optional<Goal> goal = Optional.empty();
        return goal;
    }

    private Optional<Goal> resolveFirstGoalFromResultSet(ResultSet result) throws SQLException {
        Optional<Goal> goal = Optional.empty();

        while (result.next()) {
            goal = Optional.of(mapResultSetToGoal(result));
        }

        return goal;
    }

    private List<Goal> resolveGoalsFromResultSet(ResultSet result) throws SQLException {
        List<Goal> goals = new ArrayList<Goal>();

        while (result.next()) {
            goals.add(mapResultSetToGoal(result));
        }

        return goals;
    }

    private Goal mapResultSetToGoal(ResultSet result) throws SQLException {
        long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        String title = result.getString("title");
        String description = result.getString("description");
        String deadlineString = result.getString("deadline");
        LocalDateTime deadline = LocalDateTime.parse(deadlineString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        long userId = result.getLong("user_id");
        String createdAtString = result.getString("created_at");
        LocalDateTime createdAt = LocalDateTime.parse(createdAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        String updatedAtString = result.getString("updated_at");
        LocalDateTime updatedAt = LocalDateTime.parse(updatedAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());

        return new Goal(id, uuid, title, description, deadline, userId, createdAt, updatedAt);
    }

    private List<Goal> loadRelationshipseForGoals(List<Goal> goals) {
        List<Goal> changedGoals = new ArrayList<Goal>();

        for (Goal goal : goals) {
            changedGoals.add(loadRelationshipseForGoal(goal));
        }

        return changedGoals;
    }

    private Goal loadRelationshipseForGoal(Goal goal) {
        Optional<CoachingStylePreference> coachingStylePreferenceOptional = coachingStylePreferenceDao.findCoachingSytlePreferenceByGoal(goal);

        if (coachingStylePreferenceOptional.isPresent()) {
            goal.setCoachingStylePreference(coachingStylePreferenceOptional.get());
        }

        Optional<Activity> latestActivityOptional = activityDao.selectLatestActivityForSubject(goal);

        if (latestActivityOptional.isPresent()) {
            goal.setLatestActivity(latestActivityOptional.get());
        }

        return goal;
    }

    protected LoggerInterface getLogger() {
        return logger;
    }
}