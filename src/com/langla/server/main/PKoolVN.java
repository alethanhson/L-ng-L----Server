package com.langla.server.main;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import com.PKoolVNDB;

/**
 * @author PKoolVN
 **/

public class PKoolVN {

    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public PKoolVN() {
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public static void close() {
        ds.close();
    }


    static {
        config.setDriverClassName(PKoolVNDB.DRIVER);
        config.setJdbcUrl(String.format(PKoolVNDB.URL, PKoolVNDB.DB_HOST, PKoolVNDB.DB_PORT, PKoolVNDB.DB_NAME));
        config.setUsername(PKoolVNDB.DB_USER);
        config.setPassword(PKoolVNDB.DB_PASSWORD);
        config.setMinimumIdle(PKoolVNDB.MIN_CONN);
        config.setMaximumPoolSize(PKoolVNDB.MAX_CONN);
        config.setMaxLifetime(1800000); // 30 minutes
        config.setIdleTimeout(1500000); // 25 minutes
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "true");
        ds = new HikariDataSource(config);
    }
}


