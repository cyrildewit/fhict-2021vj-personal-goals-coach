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
import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.datasource.MariaDBDriver;

@Component
public class SqlGoalDao implements GoalDao {
    @Autowired
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    private MariaDBDriver mariaDBDriver;

    private static final String SELECT_ALL_GOALS = "SELECT * FROM goals";
    private static final String SELECT_ALL_GOALS_FOR_USER = "SELECT * from goals WHERE user_id = ?;";
    private static final String SELECT_GOAL_BY_ID = "SELECT * FROM goals WHERE id = ?;";
    private static final String SELECT_GOAL_BY_UUID = "SELECT * FROM goals WHERE uuid = ?;";
    private static final String UPDATE_GOAL = "UPDATE goals SET title = ?, description = ?, deadline = ?, user_id = ? WHERE id = ?;";
    private static final String INSERT_GOAL = "INSERT INTO goals (uuid, title, description, deadline, user_id) VALUES (?, ?, ?, ?, ?);";
    private static final String DELETE_GOAL_BY_ID = "DELETE FROM goals WHERE id = ?;";

    private List<Goal> goals = new ArrayList<>();

    public List<Goal> selectAllGoals() {
        List<Goal> goals = new ArrayList<Goal>();

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GOALS);) {
             ResultSet result = preparedStatement.executeQuery();

            goals = resolveGoalsFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return goals;
    }

    public List<Goal> selectAllGoalsForUser(User user) {
        List<Goal> goals = new ArrayList<Goal>();

        try (Connection connection = mariaDBDriver.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GOALS_FOR_USER);) {
            preparedStatement.setLong(1, user.getId());
            ResultSet result = preparedStatement.executeQuery();

            goals = resolveGoalsFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return goals;
    }

    public Optional<Goal> findGoalById(Long id) {
        Optional<Goal> goal = Optional.empty();

        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GOAL_BY_ID);) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            goal = resolveFirstGoalFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return goal;
    }

    public Optional<Goal> findGoalByUuid(UUID uuid) {
        Optional<Goal> goal = Optional.empty();

        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GOAL_BY_UUID);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            goal = resolveFirstGoalFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return goal;
    }

    public void insertGoal(Goal goal) {
        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GOAL);) {
            preparedStatement.setString(1, goal.getUuid().toString());
            preparedStatement.setString(2, goal.getTitle());
            preparedStatement.setString(3, goal.getDescription());
            preparedStatement.setString(4, goal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, goal.getUserId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }
    }

    public boolean updateGoal(Goal goal) {
        boolean rowUpdated = false;

        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GOAL);) {
            preparedStatement.setString(1, goal.getTitle());
            preparedStatement.setString(2, goal.getDescription());
            preparedStatement.setString(3, goal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            rowUpdated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return rowUpdated;
    }

    public void deleteGoalById(Long id) {
        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_GOAL_BY_ID);) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }
    }

    public void deleteGoal(Goal goal) {
        deleteGoalById(goal.getId());
    }

    public Long getTotalGoalsCountForUser(User user) {
        return goals.stream().count();
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
        Long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        String title = result.getString("title");
        String description = result.getString("description");
        String deadlineString = result.getString("deadline");
        LocalDateTime deadline = LocalDateTime.parse(deadlineString, dateTimeFormatters.getMariaDbDateTimeFormatter());

        return new Goal(id, uuid, title, description, deadline);
    }
}