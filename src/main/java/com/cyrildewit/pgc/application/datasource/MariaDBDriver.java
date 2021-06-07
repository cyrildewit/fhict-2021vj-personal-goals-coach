package com.cyrildewit.pgc.application.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Component
public class MariaDBDriver implements SqlDataSourceDriver {
    private static final String JDBC_MARIADB = "jdbc:mariadb";

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    public MariaDBDriver(
            @Value("${datasource.mariadb.host}") String host,
            @Value("${datasource.mariadb.port}") String port,
            @Value("${datasource.mariadb.database}") String database,
            @Value("${datasource.mariadb.username}") String username,
            @Value("${datasource.mariadb.password}") String password
    ) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(getJdbcURL(), username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    private String getJdbcURL() {
        return String.format("%s://%s:%s/%s?useSSL=false", JDBC_MARIADB, host, port, database);
    }
}