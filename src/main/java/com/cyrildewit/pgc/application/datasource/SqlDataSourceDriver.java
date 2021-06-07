package com.cyrildewit.pgc.application.datasource;

import java.sql.Connection;

public interface SqlDataSourceDriver {
    public Connection getConnection();
}