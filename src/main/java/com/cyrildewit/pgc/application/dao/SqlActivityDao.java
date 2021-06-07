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

import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.application.datasource.MariaDBDriver;
import com.cyrildewit.pgc.application.dao.ActivityDao;

import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@Component
public class SqlActivityDao implements ActivityDao {
    private DateTimeFormatters dateTimeFormatters;

    private MariaDBDriver mariaDBDriver;

    public SqlActivityDao(DateTimeFormatters dateTimeFormatters, MariaDBDriver mariaDBDriver) {
        this.dateTimeFormatters = dateTimeFormatters;
        this.mariaDBDriver = mariaDBDriver;
    }

    private static final String SELECT_ALL_ACTIVITY_FOR_SUBJECT = "SELECT * from activities WHERE subject_id = ? AND subject_type = ?;";
    private static final String SELECT_ALL_ACTIVITY_WITHIN_PERIOD_FOR_SUBJECT = "SELECT * from activities WHERE subject_id = ? AND subject_type = ? AND subject_id = ? AND subject_type = ? AND created_at > ? AND created_at < ?;";
    private static final String SELECT_LATEST_ACTIVITY_FOR_USER = "SELECT * FROM activities WHERE subject_id = ? AND subject_type = ? ORDER BY created_at DESC LIMIT 1;";
    private static final String INSERT_ACTIVITY = "INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

    public List<Activity> selectAllActivityForSubject(Model subject) {
        List<Activity> activites = new ArrayList<Activity>();

        try (Connection connection = mariaDBDriver.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ACTIVITY_FOR_SUBJECT);) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, subject.getMorphClass());
            ResultSet result = preparedStatement.executeQuery();

            activites = resolveActiviesFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return activites;
    }

    public List<Activity> selectActivityWithinPeriodForSubjectAndCauser(Model subject, Model causer, LocalDateTime start, LocalDateTime end) {
        List<Activity> activites = new ArrayList<Activity>();

        try (Connection connection = mariaDBDriver.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ACTIVITY_WITHIN_PERIOD_FOR_SUBJECT);) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, subject.getMorphClass());
            preparedStatement.setLong(3, subject.getId());
            preparedStatement.setString(4, subject.getMorphClass());
            preparedStatement.setString(5, start.format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(6, end.format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            ResultSet result = preparedStatement.executeQuery();

            activites = resolveActiviesFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        System.out.println("Start and end" + start.format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
        System.out.println("End and end" + end.format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
        System.out.println("Activites" + activites.stream().count());

        return activites;
    }

    public Optional<Activity>  selectLatestActivityForSubject(Model subject) {
        Optional<Activity> activity = Optional.empty();

        try (Connection connection = mariaDBDriver.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LATEST_ACTIVITY_FOR_USER);) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, subject.getMorphClass());
            ResultSet result = preparedStatement.executeQuery();

            activity = resolveFirstActivityFromResultSet(result);
        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }

        return activity;
    }

    public void insertActivity(Activity activity) {
        try (Connection connection = mariaDBDriver.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACTIVITY);) {
            preparedStatement.setString(1, activity.getUuid().toString());
            preparedStatement.setString(2, activity.getLogName());
            preparedStatement.setString(3, activity.getDescription());
            preparedStatement.setLong(4, activity.getSubjectId());
            preparedStatement.setString(5, activity.getSubjectType());
            preparedStatement.setLong(6, activity.getCauserId());
            preparedStatement.setString(7, activity.getCauserType());
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(9, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            mariaDBDriver.printSQLException(e);
        }
    }

    private Optional<Activity> resolveFirstActivityFromResultSet(ResultSet result) throws SQLException {
        Optional<Activity> activity = Optional.empty();

        while (result.next()) {
            activity = Optional.of(mapResultSetToActivity(result));
        }

        return activity;
    }

    private List<Activity> resolveActiviesFromResultSet(ResultSet result) throws SQLException {
        List<Activity> activites = new ArrayList<Activity>();

        while (result.next()) {
            activites.add(mapResultSetToActivity(result));
        }

        return activites;
    }

    private Activity mapResultSetToActivity(ResultSet result) throws SQLException {
        long id = result.getLong("id");
        String uuidString = result.getString("uuid");
        UUID uuid = UUID.fromString(uuidString);
        String logName = result.getString("log_name");
        String description = result.getString("description");
        long subjectId = result.getLong("subject_id");
        String subjectType = result.getString("subject_type");
        long causerId = result.getLong("causer_id");
        String causerType = result.getString("causer_type");
        String createdAtString = result.getString("created_at");
        LocalDateTime createdAt = LocalDateTime.parse(createdAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());
        String updatedAtString = result.getString("updated_at");
        LocalDateTime updatedAt = LocalDateTime.parse(updatedAtString, dateTimeFormatters.getMariaDbDateTimeFormatter());

        return new Activity(id, uuid, logName, description, subjectId, subjectType, causerId, causerType, createdAt, updatedAt);
    }
}