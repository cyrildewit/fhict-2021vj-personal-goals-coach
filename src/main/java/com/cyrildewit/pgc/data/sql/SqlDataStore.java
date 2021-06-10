package com.cyrildewit.pgc.data.sql;

import java.sql.Connection;

public interface SqlDataStore {
    public Connection getConnection();
}