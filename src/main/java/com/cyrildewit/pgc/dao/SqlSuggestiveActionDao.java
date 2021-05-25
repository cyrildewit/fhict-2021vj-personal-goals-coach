package com.cyrildewit.pgc.dao;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Objects;
import java.time.format.DateTimeFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import com.cyrildewit.pgc.model.Goal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.model.SuggestiveAction;
import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.datasource.MariaDBDriver;
import com.cyrildewit.pgc.enums.SuggestiveActionType;

@Component
public class SqlSuggestiveActionDao implements SuggestiveActionDao {
    private DateTimeFormatters dateTimeFormatters;

    private MariaDBDriver mariaDBDriver;

    public SqlSuggestiveActionDao(DateTimeFormatters dateTimeFormatters, MariaDBDriver mariaDBDriver) {
        this.dateTimeFormatters = dateTimeFormatters;
        this.mariaDBDriver = mariaDBDriver;
    }

    private static final String INSERT_SUGGESTIVE_ACTION = "INSERT INTO suggestive_actions (uuid, type, user_id, goal_id, subgoal_id, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String SUGGESTIVE_ACTION_EXISTS = "SELECT * FROM suggestive_actions WHERE type = ? AND user_id = ? AND goal_id = ? AND subgoal_id = ? LIMIT 1;";
    private static final String SELECT_ALL_SUGGESTIVE_ACTIONS_FOR_GOAL = "SELECT * FROM suggestive_actions WHERE goal_id = ?;";
    private static final String SELECT_COUNT_SUGGESTIVE_ACTIONS_FOR_USER = "SELECT count(*) AS count FROM suggestive_actions WHERE user_id = ? AND deleted_at IS NULL;";
    private static final String SELECT_COUNT_SUGGESTIVE_ACTIONS_FOR_GOAL = "SELECT count(*) AS count FROM suggestive_actions WHERE goal_id = ? AND deleted_at IS NULL;";

    public List<SuggestiveAction> selectAllSuggestiveActionsForGoal(Goal goal) {
        List<SuggestiveAction> suggestiveActions = new ArrayList<SuggestiveAction>();

        try (Connection connection = mariaDBDriver.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SUGGESTIVE_ACTIONS_FOR_GOAL);) {
            preparedStatement.setLong(1, goal.getId());
            ResultSet result = preparedStatement.executeQuery();

            suggestiveActions = resolveSuggestiveActionsFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return suggestiveActions;
    }

    public void insertSuggestiveAction(SuggestiveAction suggestiveAction) {
        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUGGESTIVE_ACTION);) {
            preparedStatement.setString(1, suggestiveAction.getUuid().toString());
            preparedStatement.setInt(2, suggestiveAction.getType().ordinal());
            preparedStatement.setLong(3, suggestiveAction.getUserId());
            preparedStatement.setLong(4, suggestiveAction.getGoalId());
            preparedStatement.setLong(5, suggestiveAction.getSubgoalId());
            preparedStatement.setString(6, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(7, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }
    }

    public boolean suggestiveActionExists(SuggestiveAction suggestiveAction) {
        boolean exists = false;

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SUGGESTIVE_ACTION_EXISTS);) {
            preparedStatement.setInt(1, suggestiveAction.getType().ordinal());
            preparedStatement.setLong(2, suggestiveAction.getUserId());
            preparedStatement.setLong(3, suggestiveAction.getGoalId());
            preparedStatement.setLong(4, suggestiveAction.getSubgoalId());
            ResultSet result = preparedStatement.executeQuery();

            if (!resolveFirstSuggestiveActionFromResultSet(result).isEmpty()) {
                exists = true;
            };
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return exists;
    }

    public int getTotalSuggestiveActionsCountForUser(User user)
    {
        int suggestiveActionsCount = 0;

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_SUGGESTIVE_ACTIONS_FOR_USER);) {
            preparedStatement.setLong(1, user.getId());
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                suggestiveActionsCount = result.getInt("count");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return suggestiveActionsCount;
    }

    public int getTotalSuggestiveActionsCountForGoal(Goal goal)
    {
        int suggestiveActionsCount = 0;

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_SUGGESTIVE_ACTIONS_FOR_GOAL);) {
            preparedStatement.setLong(1, goal.getId());
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                suggestiveActionsCount = result.getInt("count");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return suggestiveActionsCount;
    }

    private Optional<SuggestiveAction> resolveFirstSuggestiveActionFromResultSet(ResultSet result) throws SQLException {
        Optional<SuggestiveAction> suggestiveAction = Optional.empty();

        while (result.next()) {
            suggestiveAction = Optional.of(mapResultSetToSuggestiveAction(result));
        }

        return suggestiveAction;
    }

    private List<SuggestiveAction> resolveSuggestiveActionsFromResultSet(ResultSet result) throws SQLException {
        List<SuggestiveAction> suggestiveActions = new ArrayList<SuggestiveAction>();

        while (result.next()) {
            suggestiveActions.add(mapResultSetToSuggestiveAction(result));
        }

        return suggestiveActions;
    }

    private SuggestiveAction mapResultSetToSuggestiveAction(ResultSet result) throws SQLException {
        long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        Integer typeInt = result.getInt("type");
        SuggestiveActionType type = SuggestiveActionType.values()[typeInt];
        long userId = result.getLong("user_id");
        long goalId = result.getLong("goal_id");
        long subgoalId = result.getLong("subgoal_id");
        String createdAtString = result.getString("created_at");
        LocalDateTime createdAt = LocalDateTime.parse(createdAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        String updatedAtString = result.getString("updated_at");
        LocalDateTime updatedAt = LocalDateTime.parse(updatedAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());

        return new SuggestiveAction(id, uuid, type, userId, goalId, subgoalId, createdAt, updatedAt);
    }
}