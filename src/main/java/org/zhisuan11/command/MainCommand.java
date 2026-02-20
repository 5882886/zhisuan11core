package org.zhisuan11.command;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import org.zhisuan11.core.JoinItem;
import org.zhisuan11.Zhisuan11core;
import org.zhisuan11.gui.GameMenu;
import org.zhisuan11.tasks.ScheduleTask;


public class MainCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            Player player = null;

            if (sender instanceof Player) {
                player = (Player) sender;
            }

            //   /zs help
            if (args[0].equals("help") && args.length == 1){
                sender.sendMessage(ChatColor.DARK_GRAY + "插件帮助");
                sender.sendMessage(ChatColor.BLUE + "/zs info   - 插件信息");
                sender.sendMessage(ChatColor.BLUE + "/zs reload - 重载插件配置文件");
                return true;
            }

            //   /zs info
            if (args[0].equals("info") && args.length == 1){
                sender.sendMessage(ChatColor.DARK_GRAY + "=============================================");
                sender.sendMessage(ChatColor.BLUE + "- 本插件为天津大学2024级智算11班服务器插件");
                sender.sendMessage(ChatColor.BLUE + "- 如遇bug请至Github或\"11班MC\"微信群提交");
                sender.sendMessage(ChatColor.BLUE + "- Github仓库：https://github.com/5882886/My-Minecraft-Server");
                sender.sendMessage(ChatColor.DARK_GRAY + "=============================================");
                return true;
            }

            //  /zs broadcast ...
            if (args[0].equals("broadcast")) {
                //  /zs broadcast send
                if (args[1].equals("send")) {
                    if (args.length == 2) {
                        ScheduleTask.Broadcast broadcast = new ScheduleTask.Broadcast();
                        broadcast.SendBroadcast();
                        return true;
                    } else {
                        sender.sendMessage("您输入的参数过多！");
                        return false;
                    }
                }

                sender.sendMessage("用法：/zs broadcast send");
                sender.sendMessage("您输入的命令可能有误！");
            }

            //  /zs menu ...
            if (args[0].equals("menu")) {
                if (player == null) return false;
                //  /zs menu main
                if (args[1].equals("main")) {
                    Inventory MainMenu = Zhisuan11core.main.gameMenu.createMainMenu(player);
                    GameMenu.openMenu(player, MainMenu);
                    return true;
                }
                //  /zs menu quiz
                else if (args[1].equals("quiz")) {
                    Inventory QuizMenu = Zhisuan11core.main.gameMenu.createQuizMenu(player);
                    GameMenu.openMenu(player, QuizMenu);
                    return true;
                }
            }

            //  /zs setspawn
            if (args[0].equals("setspawn") && args.length == 1) {
                if (player != null) {
                    Location location = player.getLocation();
                    player.getWorld().setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                    player.sendMessage("已将您所在的位置设为此世界出生点");
                }
                return true;
            }

            //  /zs kit ...
            if (args[0].equals("kit")) {
                Player targetPlayer;
                // /zs kit targetPlayer
                if (args.length == 2) {
                    if (player != null) {
                        targetPlayer = player.getServer().getPlayer(args[1]);
                    } else {
                        sender.sendMessage("目标玩家不存在！");
                        return false;
                    }
                } else {
                    targetPlayer = player;
                }
                // JoinItem 不是静态类，需要先创建实例再调用
                JoinItem joinItem = new JoinItem();
                joinItem.giveFirstJoinItems(targetPlayer);
                return true;
            }

            //  /zs quiz ...
            if (args[0].equals("quiz")) {
                switch (args[1]) {
                    case "send" -> {
                        if (args.length == 3) {
                            //  /zs quiz send <num>
                            int num = Integer.parseInt(args[2]);
                            Zhisuan11core.main.quiz.SendSpecificQuiz(num);
                            return true;
                        }
                        //  /zs quiz send ...
                        Zhisuan11core.main.quiz.SendQuiz();
                        return true;
                    }
                    case "show" -> {
                        //  /zs quiz show
                        int sum = Zhisuan11core.main.taskList.size();
                        sender.sendMessage("当前题库中共有" + sum + "题");
                        return true;
                    }
                    case "reload" -> {
                        Zhisuan11core.main.quiz.initialQuiz();
                        sender.sendMessage("题库初始化成功！");
                        return true;
                    }
                }
                return false;
            }

            //  /zs reload
            if (args[0].equals("reload") && args.length == 1) {
                sender.sendMessage(ChatColor.BLUE + "[zhisuan11core] 正在重载插件配置……");
                Zhisuan11core.main.reloadConfig();
                sender.sendMessage(ChatColor.BLUE + "[zhisuan11core] 配置文件重载完成！");
                return true;
            }

            sender.sendMessage("欢迎使用zhisuan11core MC服务器插件！");
            return false;

    }
}
