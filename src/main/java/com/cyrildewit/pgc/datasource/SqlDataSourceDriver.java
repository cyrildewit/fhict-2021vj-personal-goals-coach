package com.cyrildewit.pgc.datasource;

import java.sql.Connection;

public interface SqlDataSourceDriver {
    public Connection getConnection();
}