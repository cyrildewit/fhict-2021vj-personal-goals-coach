package com.cyrildewit.pgc.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cyrildewit.pgc.support.logging.LoggerInterface;

public abstract class BaseDao {
    protected abstract LoggerInterface getLogger();

    public void logSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);

                getLogger().error("SQLState: " + ((SQLException) e).getSQLState());
                getLogger().error("Error Code: " + ((SQLException) e).getErrorCode());
                getLogger().error("Message: " + e.getMessage());

                Throwable t = ex.getCause();

                while (t != null) {
                    getLogger().error("Cause: " + t);

                    t = t.getCause();
                }
            }
        }
    }
}