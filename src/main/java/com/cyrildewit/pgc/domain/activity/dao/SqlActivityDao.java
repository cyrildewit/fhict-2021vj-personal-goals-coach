package com.cyrildewit.pgc.domain.activity.dao;

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
import com.cyrildewit.pgc.domain.Model;
import com.cyrildewit.pgc.domain.activity.model.Activity;
import com.cyrildewit.pgc.domain.activity.dao.ActivityDao;

import com.cyrildewit.pgc.support.logging.LoggerInterface;
import com.cyrildewit.pgc.support.util.DateTimeFormatters;

@Component
public class SqlActivityDao extends BaseDao implements ActivityDao {
    private DateTimeFormatters dateTimeFormatters;
    private SqlDataStore sqlDataStore;
    private LoggerInterface logger;

    public SqlActivityDao(
            DateTimeFormatters dateTimeFormatters,
            MariaDBDataStore mariaDBDataStore,
            LoggerInterface logger
    ) {
        this.dateTimeFormatters = dateTimeFormatters;
        this.sqlDataStore = mariaDBDataStore;
        this.logger = logger;
    }

    private static final String SELECT_ALL_ACTIVITY_FOR_SUBJECT = "SELECT * from activities WHERE subject_id = ? AND subject_type = ?;";
    private static final String SELECT_ALL_ACTIVITY_WITHIN_PERIOD_FOR_SUBJECT = "SELECT * from activities WHERE subject_id = ? AND subject_type = ? AND subject_id = ? AND subject_type = ? AND created_at > ? AND created_at < ?;";
    private static final String SELECT_LATEST_ACTIVITY_FOR_USER = "SELECT * FROM activities WHERE subject_id = ? AND subject_type = ? ORDER BY created_at DESC LIMIT 1;";
    private static final String SELECT_ACTIVITY_BY_UUID = "SELECT * FROM activities WHERE uuid = ?;";
    private static final String INSERT_ACTIVITY = "INSERT INTO activities (uuid, log_name, description, subject_id, subject_type, causer_id, causer_type, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
    private static final String UPDATE_ACTIVITY = "UPDATE activities SET uuid = ?, log_name = ?, description = ?, subject_id = ?, subject_type = ?, causer_id = ?, causer_type = ?, created_at = ?, updated_at = ? WHERE id = ?;";
    private static final String TRUNCATE_TABLE = "TRUNCATE activities";
    private static final String SELECT_COUNT_ACTIVITIES_FOR_SUBJECT = "SELECT count(*) AS count FROM activities WHERE subject_id = ? AND subject_type = ?;";
    private static final String SELECT_COUNT_ACTIVITIES_FOR_SUBJECT_WITHIN_PERIOD = "SELECT count(*) AS count FROM activities WHERE subject_id = ? AND subject_type = ? AND created_at > ? AND created_at < ?;";

    public List<Activity> selectAllActivityForSubject(Model subject) {
        List<Activity> activites = new ArrayList<Activity>();

        try (Connection connection = sqlDataStore.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_ACTIVITY_FOR_SUBJECT);) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, subject.getMorphClass());
            ResultSet result = preparedStatement.executeQuery();

            activites = resolveActiviesFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return activites;
    }

    public List<Activity> selectActivityWithinPeriodForSubjectAndCauser(Model subject, Model causer, LocalDateTime start, LocalDateTime end) {
        List<Activity> activites = new ArrayList<Activity>();

        try (Connection connection = sqlDataStore.getConnection();

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
            logSQLException(e);
        }

        return activites;
    }

    public Optional<Activity> selectLatestActivityForSubject(Model subject) {
        Optional<Activity> activity = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LATEST_ACTIVITY_FOR_USER);) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, subject.getMorphClass());
            ResultSet result = preparedStatement.executeQuery();

            activity = resolveFirstActivityFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        return activity;
    }

    public Optional<Activity> findActivityByUuid(UUID uuid) {
        Optional<Activity> activity = Optional.empty();

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTIVITY_BY_UUID);) {
            preparedStatement.setString(1, uuid.toString());
            ResultSet result = preparedStatement.executeQuery();

            activity = resolveFirstActivityFromResultSet(result);
        } catch (SQLException e) {
            logSQLException(e);
        }

        if (activity.isPresent()) {
            return Optional.of(activity.get());
        }

        return activity;
    }

    public void insertActivity(Activity activity) {
        try (Connection connection = sqlDataStore.getConnection();
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
            logSQLException(e);
        }
    }

    public boolean updateActivity(Activity activity) {
        boolean rowUpdated = false;
//        private static final String UPDATE_ACTIVITY = "UPDATE activities SET uuid = ?, log_name = ?, description = ?, subject_id = ?, subject_type = ?, causer_id = ?, causer_type = ?, created_at = ?, updated_at = ? WHERE id = ?;";

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACTIVITY);) {
            preparedStatement.setString(1, activity.getUuid().toString());
            preparedStatement.setString(2, activity.getLogName());
            preparedStatement.setString(3, activity.getDescription());
            preparedStatement.setLong(4, activity.getSubjectId());
            preparedStatement.setString(5, activity.getSubjectType());
            preparedStatement.setLong(6, activity.getCauserId());
            preparedStatement.setString(7, activity.getCauserType());
            preparedStatement.setString(8, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(9, LocalDateTime.now().format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setLong(10, activity.getId());

            rowUpdated = preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            logSQLException(e);
        }

        return rowUpdated;
    }

    public long getTotalActiviesCountForSubject(Model subject) {
        long activitiesCount = 0;

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_ACTIVITIES_FOR_SUBJECT);) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, subject.getMorphClass());
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                activitiesCount = result.getLong("count");
            }
        } catch (SQLException e) {
            logSQLException(e);
        }

        return activitiesCount;
    }

    public long getTotalActiviesCountForSubjectWithinPeriod(Model subject, LocalDateTime start, LocalDateTime end) {
        long activitiesCount = 0;

        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COUNT_ACTIVITIES_FOR_SUBJECT_WITHIN_PERIOD);) {
            preparedStatement.setLong(1, subject.getId());
            preparedStatement.setString(2, subject.getMorphClass());
            preparedStatement.setString(3, start.format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            preparedStatement.setString(4, end.format(dateTimeFormatters.getMariaDbDateTimeFormatter()));
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                activitiesCount = result.getLong("count");
            }
        } catch (SQLException e) {
            logSQLException(e);
        }

        return activitiesCount;
    }

    public void truncate() {
        try (Connection connection = sqlDataStore.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(TRUNCATE_TABLE);) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logSQLException(e);
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

    protected LoggerInterface getLogger() {
        return logger;
    }
}