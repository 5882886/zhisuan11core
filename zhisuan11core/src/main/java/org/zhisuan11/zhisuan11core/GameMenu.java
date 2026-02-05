package org.zhisuan11.zhisuan11core;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GameMenu {

    private final Inventory MainMenu = Bukkit.createInventory(null, 27, "主菜单");

    public Inventory createMenu(Player player) {
        // 玩家信息
        AddItemToMenu(MainMenu, 0, Material.PLAYER_HEAD, "玩家信息",
                "§7点击查看您的玩家信息",
                "",
                "§e玩家: §f" + player.getName(),
                "§e等级: §f" + player.getLevel(),
                "§e生命值: §f" + String.format("%.1f", player.getHealth()) + "❤");
        // 回城按钮
        AddItemToMenu(MainMenu, 1, Material.COMPASS, "回城按钮",
                "§7点击回到世界出生点");

        AddItemToMenu(MainMenu, 26, Material.REDSTONE_BLOCK, "关于插件",
                "点击访问插件仓库");

        return MainMenu;
    }

    // 向菜单中添加物品
    // String... 类型可以一次性添加多个字符串，但必须作为最后一个参数
    public void AddItemToMenu(Inventory menu, int slot, Material material, String itemName, String... itemLore) {
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
    }

    // 打开主菜单
    public static void openMenu(Player player, Inventory menu) {
        player.openInventory(menu);
    }
    // 关闭主菜单
    public static void closeMenu(Player player) {
        player.closeInventory();
    }

}
