package org.zhisuan11.scheduled;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import org.zhisuan11.Zhisuan11core;

import java.util.List;

public class Broadcast extends BukkitRunnable {

    public int interval = Zhisuan11core.main.getConfig().getInt("BroadCast.interval");

    @Override
    public void run() {
        SendBroadcast();
    }

    public void SendBroadcast() {
        String bool = Zhisuan11core.main.getConfig().getString("BroadCast.enabled");
        List<String> announcement = Zhisuan11core.main.getConfig().getStringList("BroadCast.content");

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            if (bool != null && bool.equals("true")) {
                for (String message : announcement) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                    Zhisuan11core.main.getServer().broadcastMessage(message);
                }
            } else {
                Zhisuan11core.main.getLogger().warning("未启用公告功能！");
            }
        }
    }

}
