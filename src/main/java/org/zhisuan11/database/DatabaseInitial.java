package org.zhisuan11.database;

import org.zhisuan11.Zhisuan11core;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitial {
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
            Zhisuan11core.main.getLogger().info("Quiz数据库初始化成功！");

        } catch (SQLException e) {
            Zhisuan11core.main.getLogger().warning("数据库初始化失败！");
            throw new RuntimeException(e);
        }
    }
}
