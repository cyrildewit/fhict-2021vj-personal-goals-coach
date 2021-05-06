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
import com.cyrildewit.pgc.model.Subgoal;
import com.cyrildewit.pgc.model.User;
import com.cyrildewit.pgc.util.DateTimeFormatters;
import com.cyrildewit.pgc.datasource.MariaDBDriver;

@Component
public class SqlSubgoalDao implements SubgoalDao {
    @Autowired
    private DateTimeFormatters dateTimeFormatters;

    @Autowired
    private MariaDBDriver mariaDBDriver;

    private static final String SELECT_ALL_SUBGOALS = "SELECT * FROM subgoals";
    private static final String SELECT_ALL_SUBGOALS_FOR_GOAL = "SELECT * FROM subgoals WHERE goal_id = ?;";
    private static final String SELECT_ALL_SUBGOALS_FOR_SUBGOAL = "SELECT * FROM subgoals WHERE parent_subgoal_id = ?;";
    private static final String SELECT_COUNT_SUBGOALS = "SELECT count(*) AS count FROM subgoals;";
    private static final String SELECT_COUNT_SUBGOALS_FOR_GOAL = "SELECT count(*) AS count FROM subgoals WHERE goal_id = ?;";
    private static final String SELECT_COUNT_SUBGOALS_FOR_SUBGOAL = "SELECT count(*) AS count FROM subgoals WHERE parent_subgoal_id = ?;";
    private static final String SELECT_SUBGOAL_BY_ID = "SELECT * FROM subgoals WHERE id = ?;";
    private static final String SELECT_SUBGOAL_BY_UUID = "SELECT * FROM subgoals WHERE uuid = ?;";
    private static final String UPDATE_SUBGOAL = "UPDATE subgoals SET title = ?, description = ?, deadline = ?, user_id = ? WHERE id = ?;";
    private static final String INSERT_SUBGOAL = "INSERT INTO subgoals (uuid, title, description, deadline, user_id) VALUES (?, ?, ?, ?, ?);";
    private static final String DELETE_SUBGOAL_BY_ID = "DELETE FROM subgoals WHERE id = ?;";

    public List<Subgoal> selectAllSubgoals() {
        List<Subgoal> subgoals = new ArrayList<Subgoal>();

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SUBGOALS);) {
             ResultSet result = preparedStatement.executeQuery();

            subgoals = resolveSubgoalsFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return subgoals;
    }

    public List<Subgoal> selectAllSubgoalsForGoal(Goal goal) {
        List<Subgoal> subgoals = new ArrayList<Subgoal>();

        try (Connection connection = mariaDBDriver.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SUBGOALS_FOR_GOAL);) {
            preparedStatement.setLong(1, goal.getId());
            ResultSet result = preparedStatement.executeQuery();

            subgoals = resolveSubgoalsFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return subgoals;
    }

    public List<Subgoal> selectAllSubgoalsForSubgoal(Subgoal subgoal) {
        List<Subgoal> subgoals = new ArrayList<Subgoal>();

        try (Connection connection = mariaDBDriver.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SUBGOALS_FOR_SUBGOAL);) {
            preparedStatement.setLong(1, subgoal.getId());
            ResultSet result = preparedStatement.executeQuery();

            subgoals = resolveSubgoalsFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return subgoals;
    }

    public Optional<Subgoal> findSubgoalById(Long id) {
        Optional<Subgoal> subgoal = Optional.empty();

        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBGOAL_BY_ID);) {
            preparedStatement.setLong(1, id);
            ResultSet result = preparedStatement.executeQuery();

            subgoal = resolveFirstSubgoalFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return subgoal;
    }

    public Optional<Subgoal> findSubgoalByUuid(UUID uuid) {
        Optional<Subgoal> subgoal = Optional.empty();

        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SUBGOAL_BY_UUID);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            subgoal = resolveFirstSubgoalFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return subgoal;
    }

    public void insertSubgoal(Subgoal subgoal) {
        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SUBGOAL);) {
            preparedStatement.setString(1, subgoal.getUuid().toString());
            preparedStatement.setString(2, subgoal.getTitle());
            preparedStatement.setString(3, subgoal.getDescription());
            preparedStatement.setString(4, subgoal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(5, subgoal.getGoalId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }
    }

    public boolean updateSubgoal(Subgoal subgoal) {
        boolean rowUpdated = false;


        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SUBGOAL);) {
            preparedStatement.setString(1, subgoal.getTitle());
            preparedStatement.setString(2, subgoal.getDescription());
            preparedStatement.setString(3, subgoal.getDeadline().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(4, subgoal.getGoalId());
            preparedStatement.setLong(5, subgoal.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return rowUpdated;
    }

    public void deleteSubgoalById(Long id) {
        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SUBGOAL_BY_ID);) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }
    }

    public void deleteSubgoal(Subgoal subgoal) {
        deleteSubgoalById(subgoal.getId());
    }

    public Integer determineSubgoalLevel(Subgoal subgoal) {
        return 3;
    }

    public Long countAllSubgoalsForGoal(Goal goal)
    {
        long subgoalsCount = 0;

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_SUBGOALS_FOR_GOAL);) {
            preparedStatement.setLong(1, goal.getId());
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                subgoalsCount = result.getLong("count");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return subgoalsCount;
    }

    public Long countAllSubgoalsForSubgoal(Subgoal subgoal) {
        long subgoalsCount = 0;

        try (Connection connection = mariaDBDriver.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_SUBGOALS_FOR_SUBGOAL);) {
            preparedStatement.setLong(1, subgoal.getId());
            ResultSet result = preparedStatement.executeQuery();

            while(result.next()) {
                subgoalsCount = result.getLong("count");
            }
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return subgoalsCount;
    }

    private Optional<Subgoal> resolveFirstSubgoalFromResultSet(ResultSet result) throws SQLException {
        Optional<Subgoal> subgoal = Optional.empty();

        while (result.next()) {
            subgoal = Optional.of(mapResultSetToSubgoal(result));
        }

        return subgoal;
    }

    private List<Subgoal> resolveSubgoalsFromResultSet(ResultSet result) throws SQLException {
        List<Subgoal> subgoals = new ArrayList<Subgoal>();

        while (result.next()) {
            subgoals.add(mapResultSetToSubgoal(result));
        }

        return subgoals;
    }

    private Subgoal mapResultSetToSubgoal(ResultSet result) throws SQLException {
        Long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        String title = result.getString("title");
        String description = result.getString("description");
        String deadlineString = result.getString("deadline");
        LocalDateTime deadline = LocalDateTime.parse(deadlineString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        Long goalId = result.getLong("goal_id");

        return new Subgoal(id, uuid, title, description, deadline, goalId);
    }
}