package org.zhisuan11.zhisuan11core;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

// 服务器刷新操作
public class SidebarUpdate extends BukkitRunnable {

    Sidebar sidebar = new Sidebar();

    @Override
    // 创建调用接口
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
           update(player);
        }
    }

    // 实时更新侧边栏
    private void update(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("ServerInfo");

        if (objective == null) {
            // 如果玩家没有记分板，重新创建
            sidebar.createSidebar(player);
            return;
        }
        // 清空旧内容（通过取消注册并重新创建）
        objective.unregister();
        objective = scoreboard.registerNewObjective(
                "ServerInfo",
                Criteria.DUMMY,
                Zhisuan11core.main.getConfig().getString("Sidebar.title", "§6§l服务器信息")
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        sidebar.updateSidebar(objective, player);
    }
}
