package org.zhisuan11.quiz;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.zhisuan11.Zhisuan11core;
import org.zhisuan11.database.DataSourceManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class QuizForSql {
    
    private final Zhisuan11core plugin = Zhisuan11core.main;
    private final DataSourceManager dataSourceManager;

    // 题库中的题目数量
    public int maxId;

    public QuizForSql(DataSourceManager dataSourceManager) {
        this.dataSourceManager = dataSourceManager;
        maxId = 0;
    }

    // 设置Quiz信息
    public void setQuiz() {
        String sql = "SELECT MAX(id) FROM quiz";

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
                plugin.quiz.question = rs.getString("question");
                plugin.quiz.answer = rs.getString("answer");

                // options 一定要初始化！
                plugin.quiz.options = new ArrayList<>();
                plugin.quiz.options.add(rs.getString("option_a"));
                plugin.quiz.options.add(rs.getString("option_b"));
                plugin.quiz.options.add(rs.getString("option_c"));
                plugin.quiz.options.add(rs.getString("option_d"));

                // 设置奖励
                String rewardName = rs.getString("reward");
                Material rewardMaterial = Material.matchMaterial(rewardName.toUpperCase());
                plugin.quiz.reward = new ItemStack(Objects.requireNonNull(rewardMaterial));

            }
        } catch (SQLException e) {
            plugin.getLogger().warning("获取问题列表失败: " + e.getMessage());
        }
    }

    public void close() {
        dataSourceManager.close();
        plugin.getLogger().info("已关闭数据库连接！");
    }
    
}