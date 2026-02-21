package org.zhisuan11.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitRunnable;
import org.zhisuan11.Zhisuan11core;

import java.util.ArrayList;
import java.util.List;

public class ClearDropItems extends BukkitRunnable {

    private String enable;
    private int interval;
    private List<Integer> warnings;
    private String message;
    private String warning;

    public ClearDropItems() {
        enable = "false";
        interval = 900;

        warnings = new ArrayList<>();
        warnings.add(60);
        warnings.add(30);
        warnings.add(10);
    }

    int secondsLeft = interval;

    @Override
    public void run() {
        for (int warning : warnings) {
            if (secondsLeft == warning) {
                broadcastWarning(warning);
                break;
            }
        }
        // 每秒执行一次
        secondsLeft--;

        if (secondsLeft <= 0) {
            clearAllWorlds();
            // 重置清理间隔
            secondsLeft = interval;
        }
    }

    // 初始化配置
    public void loadConfig() {
        enable = Zhisuan11core.main.getConfig().getString("ClearDropItems.enabled", "false");
        interval = Zhisuan11core.main.getConfig().getInt("ClearDropItems.interval", 900);
        warnings = Zhisuan11core.main.getConfig().getIntegerList("ClearDropItems.warnings");

        message = Zhisuan11core.main.getConfig().getString("ClearDropItems.message",
                "&a[zhisuan11core] &e已清理 &b%count% &e个掉落物！");
        warning = Zhisuan11core.main.getConfig().getString("ClearDropItems.warning",
                "&e[zhisuan11core] &c警告！&e物品将在 &b%seconds% &e秒后清理！");
    }

    // 清理所有世界的掉落物
    public void clearAllWorlds() {
        if (enable.equals("true")) {
            int count = 0;
            message = message.replace("%count", String.valueOf(count));

            for (World world : Bukkit.getWorlds()) {
                for (Item item : world.getEntitiesByClass(Item.class)) {
                    item.remove();
                    count ++;
                }
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    // 清理指定世界的掉落物
    public boolean clearSpecificWorlds(String worldName) {
        if (enable.equals("true")) {
            int count = 0;
            message = message.replace("%count", String.valueOf(count));

            World world = Bukkit.getWorld(worldName);
            if (world == null) return false;

            for (Item item : world.getEntitiesByClass(Item.class)) {
                item.remove();
                count ++;
            }

            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }
        return false;
    }

    // 清理前预警
    private void broadcastWarning(int seconds) {
        warning = warning.replace("%seconds%", String.valueOf(seconds));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', warning));
    }

}
