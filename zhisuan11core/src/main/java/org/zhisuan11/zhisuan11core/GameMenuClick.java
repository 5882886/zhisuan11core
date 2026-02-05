package org.zhisuan11.zhisuan11core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class GameMenuClick implements Listener {

    @EventHandler
    // 菜单点击事件
    public void MenuClick(InventoryClickEvent event) {
        if ((event.getWhoClicked() instanceof Player player)) {
            InventoryView view = event.getView();

            if (view.getTitle().equals("主菜单")) {
                // 防止玩家移动物品
                event.setCancelled(true);
                // 获取点击物品
                ItemStack clicked = event.getCurrentItem();
                if (clicked != null) {
                    switch (clicked.getType()) {
                        case PLAYER_HEAD:
                        case COMPASS:
                            player.teleport(player.getWorld().getSpawnLocation());
                            player.sendMessage("已传送至世界出生点");
                            break;
                        case REDSTONE_BLOCK:
                            player.sendMessage("插件作者：RunicDolphin806");
                            player.sendMessage("仓库地址：https://github.com/5882886/My-Minecraft-Server");
                            GameMenu.closeMenu(player);
                        default:
                            break;
                    }
                }
            }

        }

    }
}
