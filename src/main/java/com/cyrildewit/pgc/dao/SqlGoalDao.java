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

import com.cyrildewit.pgc.models.Goal;
import com.cyrildewit.pgc.models.User;
import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.datasource.MariaDBDriver;

@Component
public class SqlGoalDao implements GoalDao {
    @Autowired
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    private MariaDBDriver mariaDBDriver;

    private static final String SELECT_ALL_GOALS = "select * from goals";
    private static final String SELECT_ALL_GOALS_FOR_USER = "select * from goals where user_id = ?;";
    private static final String SELECT_GOAL_BY_ID = "select * from goals where id = ?;";
    private static final String SELECT_GOAL_BY_UUID = "select * from goals where uuid = ?;";

    private List<Goal> goals = new ArrayList<>();

    public List<Goal> selectAllGoals() {

        List<Goal> goals = new ArrayList<>();

        try (Connection connection = mariaDBDriver.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GOALS);) {
             System.out.println(preparedStatement);
             ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                Long id = rs.getLong("id");
                String uuidString = rs.getString("uuid");
                UUID uuid = UUID.fromString(uuidString);
                String title = rs.getString("title");
                String description = rs.getString("description");
                String deadlineString = rs.getString("deadline");
                LocalDateTime deadline = LocalDateTime.parse(deadlineString, dateTimeFormatters.getMariaDbDateTimeFormatter());
                goals.add(new Goal(id, uuid, title, description, deadline));
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return goals;
    }

    public List<Goal> selectAllGoalsForUser(User user) {
        List<Goal> goals = new ArrayList<>();

        try (Connection connection = mariaDBDriver.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_GOALS_FOR_USER);) {
            preparedStatement.setLong(1, user.getId());
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                Long id = rs.getLong("id");
                String uuidString = rs.getString("uuid");
                UUID uuid = UUID.fromString(uuidString);
                String title = rs.getString("title");
                String description = rs.getString("description");
                String deadlineString = rs.getString("deadline");
                LocalDateTime deadline = LocalDateTime.parse(deadlineString, dateTimeFormatters.getMariaDbDateTimeFormatter());
                goals.add(new Goal(id, uuid, title, description, deadline));
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return goals;
    }

    public Optional<Goal> findGoalById(Long id) {
        Goal goal = null;

        try (Connection connection = mariaDBDriver.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GOAL_BY_ID);) {
            preparedStatement.setLong(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                Long goalId = rs.getLong("id");
                String uuidString = rs.getString("uuid");
                UUID uuid = UUID.fromString(uuidString);
                String title = rs.getString("title");
                String description = rs.getString("description");
                String deadlineString = rs.getString("deadline");
                LocalDateTime deadline = LocalDateTime.parse(deadlineString, dateTimeFormatters.getMariaDbDateTimeFormatter());
                goal = new Goal(goalId, uuid, title, description, deadline);
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return Optional.of(goal);
    }

    public Optional<Goal> findGoalByUuid(UUID uuid) {
        Goal goal = null;

        try (Connection connection = mariaDBDriver.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_GOAL_BY_UUID);) {
            preparedStatement.setString(1, uuid.toString());
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            // Step 4: Process the ResultSet object.
            while (rs.next()) {
                Long id = rs.getLong("id");
                String uuidString = rs.getString("uuid");
                UUID goalUuid = UUID.fromString(uuidString);
                String title = rs.getString("title");
                String description = rs.getString("description");
                String deadlineString = rs.getString("deadline");
                LocalDateTime deadline = LocalDateTime.parse(deadlineString, dateTimeFormatters.getMariaDbDateTimeFormatter());
                goal = new Goal(id, goalUuid, title, description, deadline);
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return Optional.of(goal);
    }

    public void insertGoal(Goal goal) {
        goals.add(goal);
    }

    public void updateGoal(Goal goal, String[] params) {
//        goal.setName(Objects.requireNonNull(
//                params[0], "Name cannot be null"));
//        goal.setEmail(Objects.requireNonNull(
//                params[1], "Email cannot be null"));

        goals.add(goal);
    }

    public void deleteGoalById(Long id) {
        Optional<Goal> goal = goals.stream()
                .filter(streamGoal -> id.equals(streamGoal.getId()))
                .findAny();

        if (goal.isPresent()) {
            goals.remove(goal);
        }
    }

    public void deleteGoal(Goal goal) {
        goals.remove(goal);
    }

    public Long getTotalGoalsCountForUser(User user) {
        return goals.stream().count();
    }
}