package org.zhisuan11.quiz;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
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

    private int interval;
    private int maxNum;
    private String storageType;

    /* ----- 发送问题 ----- */
    // 问题回答界面在 /gui/GameMenu.java
    // 发送随机问题
    public void SendQuiz() {
        // 设置随机数
        Random random = new Random();
        int randomNumber = 0;
        if (storageType.equals("YAML")) {
            randomNumber = random.nextInt(plugin.taskList.size());
            plugin.task = plugin.taskList.get(randomNumber);
        } else if (storageType.equals("MYSQL")) {
            // 获取 [1, maxId] 之间的随机数
            // random.nextInt(b-a+1)+1 获取 [a, b] 之间的随机整数
            randomNumber = random.nextInt(plugin.databaseStorage.maxId) + 1;
            plugin.databaseStorage.getQuizById(randomNumber);
        }

        if (plugin.task == null) {
            plugin.getLogger().warning("Task为空，请检查文件存储！");
            return;
        }
        // 设置回答的玩家数据
        // 在发送问题时再对玩家数据初始化，否则会造成错误记录
        plugin.task.answeredPlayers = new HashSet<>();
        plugin.task.winner = null;

        SendText();
        plugin.getLogger().info("已发送编号为" + randomNumber +"的Quiz！");
    }

    // 发送特定问题
    public void SendSpecificQuiz(int num) {
        if (storageType.equals("YAML")) {
            plugin.task = plugin.taskList.get(num);
        } else if (storageType.equals("MYSQL")) {
            plugin.databaseStorage.getQuizById(num);
        }

        if (plugin.task == null) {
            plugin.getLogger().warning("Task为空，请检查文件存储！");
            return;
        }

        plugin.task.answeredPlayers = new HashSet<>();
        plugin.task.winner = null;
        SendText();
    }

    // 发送文本组件
    private void SendText() {
        // 创建可点击的文本组件
        TextComponent message = new TextComponent("§6§l新一轮Quiz开始啦！\n【点击这里】§r§e打开Quiz界面！");
        // 点击事件：玩家点击后会自动输入并执行此命令
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zs menu quiz"));
        // 悬浮文本
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7点击参加Quiz！")));
        Zhisuan11core.main.getServer().spigot().broadcast(message);
    }

    // 检查回答事件
    public void check(Player player, String response) {
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
        if (response.equals(plugin.task.answer)) {
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

    /* ---- 问题生成 ----- */
    // 初始化Quiz
    public void initialQuiz() {
        QuizForYaml quizForYaml = new QuizForYaml();

        // 生成Quiz.yml文件
        plugin.QuizFile = new File(plugin.getDataFolder(), "Quiz.yml");
        if (!plugin.QuizFile.exists()) {
            plugin.saveResource("Quiz.yml", false); // 假设你在jar包的根目录放了默认文件
        }
        plugin.QuizConfig = YamlConfiguration.loadConfiguration(plugin.QuizFile);

        // 获取设置参数
        interval = plugin.QuizConfig.getInt("config.interval", 900);
        storageType = plugin.QuizConfig.getString("config.storage", "YAML");

        // 设置Quiz存储格式
        if (storageType.equals("YAML")) {
            plugin.taskList = new ArrayList<>();
            plugin.taskMap = plugin.QuizConfig.getMapList("List");
            quizForYaml.setQuiz();
            maxNum = plugin.taskList.size();
            plugin.getLogger().info("Quiz使用YAML格式");
        } else if (storageType.equals("MYSQL")) {
            plugin.databaseConfig = new QuizForSql.DatabaseConfig();
            // 设置数据库信息
            plugin.databaseConfig.setType(plugin.QuizConfig.getString("config.storage", "mysql"));
            plugin.databaseConfig.setHost(plugin.QuizConfig.getString("config.host", "localhost"));
            plugin.databaseConfig.setPort(plugin.QuizConfig.getInt("config.port", 3306));
            plugin.databaseConfig.setDatabase(plugin.QuizConfig.getString("config.database", "quiz"));
            plugin.databaseConfig.setUsername(plugin.QuizConfig.getString("config.name", "root"));
            plugin.databaseConfig.setPassword(plugin.QuizConfig.getString("config.password", ""));
            plugin.databaseConfig.setUseSSL(plugin.QuizConfig.getBoolean("config.useSSL", false));

            QuizForSql.DataSourceManager dataSourceManager = new QuizForSql.DataSourceManager(plugin.databaseConfig);
            QuizForSql.DatabaseInitial databaseInitial = new QuizForSql.DatabaseInitial(dataSourceManager);
            databaseInitial.InitialTables();

            plugin.databaseStorage = new QuizForSql.DatabaseStorage(dataSourceManager);
            plugin.databaseStorage.setQuiz();

            maxNum = plugin.databaseStorage.maxId;

            plugin.getLogger().info("Quiz使用MYSQL格式");
        }
    }

    public int getMaxNum() {
        return maxNum;
    }

    public int getInterval() {
        return interval;
    }
}
