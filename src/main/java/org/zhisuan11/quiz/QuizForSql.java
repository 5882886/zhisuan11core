package org.zhisuan11.quiz;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.zhisuan11.Zhisuan11core;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class QuizForSql {

    // 数据库连接设置
    public static class DatabaseConfig  {
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;
        private boolean useSSL;
        private String type;

        // 默认构造函数
        public DatabaseConfig() {
            this.type = "mysql";
            this.host = "localhost";
            this.port = 3306;
            this.database = "quiz";
            this.username = "root";
            this.password = "";
            this.useSSL = false;
        }

        // 接口
        // public String getHost() {return host;}
        public void setHost(String host) {this.host = host;}

        // public int getPort() {return port;}
        public void setPort(int port) {this.port = port;}

        // public String getDatabase() {return database;}
        public void setDatabase(String database) {this.database = database;}

        public String getUsername() {return username;}
        public void setUsername(String username) {this.username = username;}

        public String getPassword() {return password;}
        public void setPassword(String password) {this.password = password;}

        // public boolean isUseSSL() {return useSSL;}
        public void setUseSSL(boolean ssl) {this.useSSL = ssl;}

        public String getType() {return type;}
        public void setType(String type) {this.type = type;}

        public String getJdbcUrl() {
            if (type.equalsIgnoreCase("mysql")) {
                return String.format("jdbc:mysql://%s:%d/%s?useSSL=%s&serverTimezone=UTC",
                        host, port, database, useSSL);
            } else {
                return "jdbc:sqlite:plugins/QuizPlugin/quiz.db";
            }
        }
    }

    public static class DataSourceManager {
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

    // 初始化连接
    public static class DatabaseInitial {
        private final DataSourceManager dataSourceManager;

        public DatabaseInitial(DataSourceManager dataSourceManager) {
            this.dataSourceManager = dataSourceManager;
        }

        public void InitialTables() {
            try(Connection connection = dataSourceManager.getConnection();
                Statement statement = connection.createStatement()) {

                // 创建问题表
                String createQuizTable = """
                    CREATE TABLE IF NOT EXISTS quiz (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        question TEXT NOT NULL,
                        option_a VARCHAR(255) NOT NULL,
                        option_b VARCHAR(255) NOT NULL,
                        option_c VARCHAR(255) NOT NULL,
                        option_d VARCHAR(255) NOT NULL,
                        answer CHAR(1) NOT NULL,
                        reward VARCHAR(255) NOT NULL
                    )
                    """;

                statement.execute(createQuizTable);
                Zhisuan11core.main.getLogger().info("数据库表初始化成功！");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 数据库存储类
    public static class DatabaseStorage {

        private final DataSourceManager dataSourceManager;
        private final Zhisuan11core plugin = Zhisuan11core.main;

        // 题库中的题目数量
        public int maxId;

        public DatabaseStorage(DataSourceManager dataSourceManager) {
            this.dataSourceManager = dataSourceManager;
        }

        // 设置Quiz信息
        public void setQuiz() {
            String sql = "SELECT MAX(id) FROM quiz";
            maxId = 0;

            try (Connection connection = dataSourceManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql);
                 ResultSet rs = statement.executeQuery()) {

                // 获取最大的id
                if (rs.next()) {
                    maxId = rs.getInt(1);
                }

                plugin.getLogger().info("问题列表初始化成功！");
                plugin.getLogger().info("当前题库共有" + maxId + "题");

            } catch (SQLException e) {
                plugin.getLogger().warning("获取问题列表失败: " + e.getMessage());
            }
        }

        // 获取特定的Quiz
        public void getQuizById(int id) {
            String sql = "SELECT * FROM quiz WHERE id = ?";

            try (Connection connection = dataSourceManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setInt(1, id);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    plugin.task.question = rs.getString("question");
                    plugin.task.answer = rs.getString("answer");

                    // options 一定要初始化！
                    plugin.task.options = new ArrayList<>();
                    plugin.task.options.add(rs.getString("option_a"));
                    plugin.task.options.add(rs.getString("option_b"));
                    plugin.task.options.add(rs.getString("option_c"));
                    plugin.task.options.add(rs.getString("option_d"));

                    // 设置奖励
                    String rewardName = rs.getString("reward");
                    Material rewardMaterial = Material.matchMaterial(rewardName.toUpperCase());
                    plugin.task.reward = new ItemStack(Objects.requireNonNull(rewardMaterial));

                }
            } catch (SQLException e) {
                plugin.getLogger().warning("获取问题列表失败: " + e.getMessage());
            }
        }

        public void close() {
            dataSourceManager.close();
        }
    }
}