package org.zhisuan11.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

// 数据库连接
public class DataSourceManager {
    private HikariDataSource dataSource;
    private final DatabaseConfig databaseConfig;

    public DataSourceManager(DatabaseConfig databaseConfig) {
        this.databaseConfig = databaseConfig;
        setupDataSource();
    }

    private void setupDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(databaseConfig.getJdbcUrl());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        if (databaseConfig.getType().equalsIgnoreCase("MYSQL")) {
            config.setUsername(databaseConfig.getUsername());
            config.setPassword(databaseConfig.getPassword());
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        }

        dataSource = new HikariDataSource(config);
    }

    // 获取连接
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 取消连接
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
