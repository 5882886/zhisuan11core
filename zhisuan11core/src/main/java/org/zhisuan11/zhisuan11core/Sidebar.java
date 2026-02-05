package org.zhisuan11.zhisuan11core;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public class Sidebar {

    // 创建新侧边栏
    public void createSidebar(Player player) {
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(
                "ServerInfo",
                Criteria.DUMMY,
                Zhisuan11core.main.getConfig().getString("Sidebar.title", "§6§l服务器信息")
        );
        // 设置显示位置
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        updateSidebar(objective, player);
        // 玩家显示侧边栏
        player.setScoreboard(scoreboard);
    }

    // 侧边栏内容封装函数
    public void setSidebar(Objective objective, String text, int score) {
        Score scoreEntry = objective.getScore(text);
        scoreEntry.setScore(score);
    }

    // 侧边栏生成
    public void updateSidebar(Objective objective, Player player) {
        setSidebar(objective, "§9", 9);
        setSidebar(objective, "§7欢迎，§a" + player.getName() + " ！", 8);
        setSidebar(objective, "§7", 7);
        setSidebar(objective, "§7在线玩家: §a" + Bukkit.getOnlinePlayers().size(), 6);
        setSidebar(objective, "§5", 5);
        setSidebar(objective, "§7服务器TPS: " + getAllTPS(), 4);
        setSidebar(objective, "§3", 3);
        setSidebar(objective, "§7你的延迟: §a" + player.getPing() + "ms", 2);
        setSidebar(objective, "§1", 1);
        setSidebar(objective, Zhisuan11core.main.getConfig().getString("Sidebar.ip","www.example.com"), 0);
    }

    // 获取服务器 TPS
    public String getAllTPS() {
        try {
            Server server = Bukkit.getServer();
            double[] tps = (double[]) server.getClass()
                    .getMethod("getTPS")
                    .invoke(server);
            return String.format("%.1f", Math.min(tps[0], 20.0));
        } catch (Exception e) {
            return "20.0";
        }
    }
}
