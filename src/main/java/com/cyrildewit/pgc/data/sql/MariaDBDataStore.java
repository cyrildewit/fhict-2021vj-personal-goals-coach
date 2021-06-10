package com.cyrildewit.pgc.data.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Component
public class MariaDBDataStore implements SqlDataStore {
    private static final String JDBC_MARIADB = "jdbc:mariadb";

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    public MariaDBDataStore(
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

    private String getJdbcURL() {
        return String.format("%s://%s:%s/%s?useSSL=false", JDBC_MARIADB, host, port, database);
    }
}