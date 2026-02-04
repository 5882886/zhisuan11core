package org.zhisuan11.zhisuan11core;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.util.List;
import static org.bukkit.Bukkit.getServer;


public class Zhisuan11Command_main implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //    /zhisuan11 ……或 /zs ……

        Player player = (Player) sender;
        FileConfiguration config = Zhisuan11core.main.getConfig();

        //   指令/zs help
        if (args.length == 1 && args[0].equals("help")){
            sender.sendMessage(ChatColor.DARK_GRAY + "插件帮助");
            sender.sendMessage(ChatColor.BLUE + "/zs info   - 插件信息");
            sender.sendMessage(ChatColor.BLUE + "/zs reload - 重载插件配置文件");
            return true;
        }

        //   指令/zs info
        if (args.length == 1 && args[0].equals("info")){
            sender.sendMessage(ChatColor.DARK_GRAY + "插件信息");
            sender.sendMessage(ChatColor.BLUE + " - 本插件为天津大学2024级智算11班服务器插件");
            sender.sendMessage(ChatColor.BLUE + " - 如遇bug请至Github或\"11班MC\"微信群提交");
            sender.sendMessage(ChatColor.DARK_GRAY + " - Github仓库：https://github.com/5882886/My-Minecraft-Server");
            return true;
        }

        if (args[0].equals("broadcast")) {
            if(args.length == 1) {
                sender.sendMessage("用法：/zs broadcast …");
                return true;
            }
            //  指令/zs broadcast send
            else if (args.length == 2) {
                List<String> announcement = config.getStringList("BroadCast.content");
                for (String message : announcement) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                    getServer().broadcastMessage(message);
                }
                return true;
            }
        }

        if (args.length == 1 && args[0].equals("setspawn")) {
            Location location = player.getLocation();
            player.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
            player.sendMessage("已将您所在的位置设为此世界出生点");
            return true;
        }

        // 指令 /zs kit targetPlayer
        else if (args.length == 2 && args[0].equals("kit")) {
            Player targetPlayer = player.getServer().getPlayer(args[1]);
            // JoinItem 不是静态类，需要先创建实例再调用
            JoinItem joinItem = new JoinItem();
            joinItem.giveFirstJoinItems(targetPlayer);
            return true;
        }

        //   指令/zs reload
        if (args.length == 1 && args[0].equals("reload")) {
            if (sender != null) {
                sender.sendMessage(ChatColor.BLUE + "[zhisuan11core]正在重载插件配置……");
                Zhisuan11core.main.reloadConfig();
                sender.sendMessage(ChatColor.BLUE + "[zhisuan11core]配置文件重载完成！");
            }
            else {
                Zhisuan11core.main.getLogger().info(ChatColor.BLUE + "[zhisuan11core]正在重载插件配置……");
                Zhisuan11core.main.reloadConfig();
                Zhisuan11core.main.getLogger().info(ChatColor.BLUE + "[zhisuan11core]配置文件重载完成！");
            }
            return true;
        }


        else {
            sender.sendMessage("欢迎使用zhisuan11core MC服务器插件！");
        }

        return false;
    }
}
