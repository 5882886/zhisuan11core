package org.zhisuan11.scheduled;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.zhisuan11.Zhisuan11core;
import org.zhisuan11.gui.GameMenu;

import java.io.File;
import java.util.*;


public class Quiz extends BukkitRunnable {

    @Override
    public void run() {
        SendQuiz();
    }

    private final Zhisuan11core plugin = Zhisuan11core.main;

    // 题目类
    public static class Task {
        public String question;
        public List<String> options;
        public String answer;
        public ItemStack reward;

        // 回答玩家数据记录
        public Set<UUID> answeredPlayers;
        public UUID winner;
    }

    // 间隔时间
    int interval = plugin.QuizConfig.getInt("config.interval", 900);

    // 设置问答池
    public void setQuiz() {
        if (plugin.taskMap == null || plugin.taskMap.isEmpty()) {
            Zhisuan11core.main.getLogger().warning("问答池为空！");
            return;
        }

        for (Map<?, ?> taskMap : plugin.taskMap) {
            Task temp = new Task();

            temp.question = (String) taskMap.get("Question");
            temp.answer = (String) taskMap.get("Answer");

            Map<?, ?> options = (Map<?, ?>) taskMap.get("Options");
            temp.options = new ArrayList<>();
            temp.options.add((String) options.get("A"));
            temp.options.add((String) options.get("B"));
            temp.options.add((String) options.get("C"));
            temp.options.add((String) options.get("D"));

            // 设置奖品
            Object rewardObj = taskMap.get("Reward");
            String rewardName = rewardObj.toString();
            Material rewardMaterial = Material.matchMaterial(rewardName.toUpperCase());
            temp.reward = new ItemStack(Objects.requireNonNull(rewardMaterial));

            plugin.taskList.add(temp);
        }
    }

    // 选择问题
    public Task chooseQuiz() {
        Random random = new Random();
        return plugin.taskList.get(random.nextInt(plugin.taskList.size()));
    }

    // 问题回答界面在 /gui/GameMenu.java
    // 发送随机问题
    public void SendQuiz() {
        setQuiz();
        plugin.task = chooseQuiz();
        // 设置回答的玩家数据
        // 在发送问题时再对玩家数据初始化，否则会造成错误记录
        plugin.task.answeredPlayers = new HashSet<>();
        plugin.task.winner = null;
        // 创建可点击的文本组件
        TextComponent message = new TextComponent("§6§l新一轮Quiz开始啦！\n【点击这里】§r§e打开Quiz界面！");
        // 点击事件：玩家点击后会自动输入并执行此命令
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zs menu quiz"));
        // 悬浮文本
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7点击参加Quiz！")));
        Zhisuan11core.main.getServer().spigot().broadcast(message);
    }

    // 发送特定问题
    public void SendSpecificQuiz(int num) {
        setQuiz();
        plugin.task = plugin.taskList.get(num);
        plugin.task.answeredPlayers = new HashSet<>();
        plugin.task.winner = null;

        TextComponent message = new TextComponent("§6§l【点击这里】§r§e打开Quiz界面！");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zs menu quiz"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7点击参加Quiz！")));

        Zhisuan11core.main.getServer().spigot().broadcast(message);
    }

    // 重载配置文件
    public void reloadQuiz() {
        plugin.QuizFile = new File(plugin.getDataFolder(), "Quiz.yml");
        if (!plugin.QuizFile.exists()) {
            plugin.saveResource("Quiz.yml", false);
        }
        plugin.QuizConfig = YamlConfiguration.loadConfiguration(plugin.QuizFile);
        plugin.taskMap = plugin.QuizConfig.getMapList("List");
    }

    // 检查回答事件
    public void check(Player player) {
        // 获取玩家UUID
        UUID playerId = player.getUniqueId();

        // 如果已经有玩家回答正确
        if (plugin.task.winner != null) {
            player.sendMessage(plugin.QuizConfig.getString("message.hasCorrect", "已有玩家回答正确！"));
            GameMenu.closeMenu(player);
            return;
        }

        // 检查玩家是否已经回答过
        if (plugin.task.answeredPlayers.contains(playerId)) {
            player.sendMessage(plugin.QuizConfig.getString("message.hasAnswered", "您已经回答过此问题！"));
            GameMenu.closeMenu(player);
            return;
        }

        // 记录玩家已回答
        plugin.task.answeredPlayers.add(playerId);

        // 回答正确
        if (plugin.response.equals(plugin.task.answer)) {
            // 给予奖励
            player.getInventory().addItem(plugin.task.reward);
            // 发送提示音效
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            GameMenu.closeMenu(player);
            plugin.task.winner = playerId;
            player.sendMessage(plugin.QuizConfig.getString("message.correct", "回答正确，奖励已发放！"));
            return;
        }

        // 回答错误
        player.sendMessage(plugin.QuizConfig.getString("message.wrong", "回答错误！"));
        GameMenu.closeMenu(player);
    }
}
