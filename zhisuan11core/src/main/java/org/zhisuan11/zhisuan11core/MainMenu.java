package org.zhisuan11.zhisuan11core;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class MainMenu {

    public Inventory createMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, "主菜单");

        // 玩家信息
        menu = createMenu(0, menu, Material.PLAYER_HEAD, "玩家信息",
                "§7点击查看你的玩家信息", "",
                "§e玩家: §f" + player.getName(),
                "§e等级: §f" + player.getLevel(),
                "§e生命值: §f" + String.format("%.1f", player.getHealth()) + "❤");
        // 回城按钮
        menu = createMenu(1, menu, Material.COMPASS, "回城按钮",
                "§7点击回到世界出生点");

        return menu;
    }

    // 向菜单中添加物品
    // String... 类型可以一次性添加多个字符串，但必须作为最后一个参数
    public Inventory createMenu(int slot, Inventory menu, Material material, String itemName, String... itemLore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(itemName);
            if (itemLore != null && itemLore.length > 0) {
                itemMeta.setLore(Arrays.asList(itemLore));
            }
        }
        itemStack.setItemMeta(itemMeta);
        menu.setItem(slot, itemStack);
        return menu;
    }

    // 打开主菜单
    public void openMenu(Player player, Inventory menu) {
        player.openInventory(menu);
    }

}
