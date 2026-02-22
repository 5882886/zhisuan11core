package org.zhisuan11.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.zhisuan11.Zhisuan11core;

import java.util.List;

// 启动插件定时执行的内容
public class ScheduleTask {

    private final Zhisuan11core plugin = Zhisuan11core.main;

    public void ScheduleTasks() {

        // 游戏内侧边栏
        plugin.sidebar.startUpdate();
        // 发送Quiz
        plugin.quiz.SendTask();
        // 清理掉落物
        plugin.clearDropItems.startClear();

        // 服务器公告
        Broadcast broadcast = new Broadcast();
        broadcast.runTaskTimer(plugin, 20L * 15, 20L * broadcast.getInterval());
        // 检查玩家延迟
        CheckPing checkPing = new CheckPing();
        checkPing.runTaskTimer(plugin, 0L, 20L * 60);
    }


    // 服务器公告
    public static class Broadcast extends BukkitRunnable {

        private String enable;
        private int interval;

        public Broadcast() {
            enable = "true";
            interval = 900;
        }

        public int getInterval() {
            return interval;
        }

        @Override
        public void run() {
            SendBroadcast();
        }

        public void SendBroadcast() {
            enable = Zhisuan11core.main.getConfig().getString("Broadcast.enabled", "true");

            if (!Bukkit.getOnlinePlayers().isEmpty()) {
                if (enable.equals("true")) {
                    interval = Zhisuan11core.main.getConfig().getInt("Broadcast.interval", 900);
                    List<String> announcement = Zhisuan11core.main.getConfig().getStringList("Broadcast.content");
                    for (String message : announcement) {
                        message = ChatColor.translateAlternateColorCodes('&', message);
                        Zhisuan11core.main.getServer().broadcastMessage(message);
                    }
                } else {
                    Zhisuan11core.main.getLogger().warning("未启用公告功能！");
                }
            } else {
                Zhisuan11core.main.getLogger().info("当前服务器无玩家在线，暂停公告发送！");
            }
        }

    }

    // 检查玩家延迟
    public static class CheckPing extends BukkitRunnable {
        @Override
        public void run() {
            for (Player player : Bukkit.getOnlinePlayers()) {
                checkPing(player);
            }
        }

        private void checkPing(Player player) {
            if (player.getPing() > 300) {
                player.sendMessage("您的延迟较大，请检查网络连接……");
            }
        }
    }
}
