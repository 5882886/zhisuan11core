package org.zhisuan11.zhisuan11core;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class Respawn implements Listener {
    @EventHandler

    public void spawn(PlayerDeathEvent event) {
        event.getEntity().spigot().respawn();

        // 获取世界出生点
        Location spawnLocation = event.getEntity().getWorld().getSpawnLocation();

        // 传送玩家到出生点
        event.getEntity().teleport(spawnLocation);
    }
}
