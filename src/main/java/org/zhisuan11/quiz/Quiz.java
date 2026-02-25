package org.zhisuan11.quiz;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import org.zhisuan11.Zhisuan11core;
import org.zhisuan11.database.DataSourceManager;
import org.zhisuan11.database.DatabaseInitial;

import java.io.File;
import java.util.*;


public class Quiz {

    private final Zhisuan11core plugin = Zhisuan11core.main;
    private BukkitTask send;

    // 问题内容
    public String question;
    public List<String> options;
    public String answer;
    public ItemStack reward;

    // 回答玩家数据记录
    private Set<UUID> answeredPlayers;
    private UUID winner;

    public Quiz() {
        this.options = new ArrayList<>();
        this.answeredPlayers = new HashSet<>();
    }

    // 定时任务
    public void SendTask() {
        if (send != null) {
            send.cancel();
        }

        send = new BukkitRunnable() {
            @Override
            public void run() {
                SendQuiz();
            }
        }.runTaskTimer(plugin, 0L, 20L * plugin.quizConfig.getInterval());
    }

    /* ----- 发送问题 ----- */
    // 问题回答界面在 /gui/GameMenu.java
    // 发送随机问题
    public void SendQuiz() {
        // 设置随机数
        Random random = new Random();
        int randomNumber = 0;
        if (plugin.quizConfig.getStorageType().equals("YAML")) {
            randomNumber = random.nextInt(plugin.quizConfig.QuizList.size());
            plugin.quiz = plugin.quizConfig.QuizList.get(randomNumber);
        } else if (plugin.quizConfig.getStorageType().equals("MYSQL")) {
            // 获取 [1, maxId] 之间的随机数
            // random.nextInt(b-a+1)+1 获取 [a, b] 之间的随机整数
            randomNumber = random.nextInt(plugin.quizForSql.maxId) + 1;
            plugin.quizForSql.getQuizById(randomNumber);
        }

        if (plugin.quiz == null) {
            plugin.getLogger().warning("Exercise为空，请检查文件存储！");
            return;
        }
        // 设置回答的玩家数据
        // 在发送问题时再对玩家数据初始化，否则会造成错误记录
        plugin.quiz.answeredPlayers = new HashSet<>();
        plugin.quiz.winner = null;

        SendText();
        plugin.getLogger().info("已发送编号为" + randomNumber +"的Quiz！");
    }

    // 发送特定问题
    public void SendSpecificQuiz(int num) {
        if (plugin.quizConfig.getStorageType().equals("YAML")) {
            plugin.quiz = plugin.quizConfig.QuizList.get(num);
        } else if (plugin.quizConfig.getStorageType().equals("MYSQL")) {
            plugin.quizForSql.getQuizById(num);
        }

        if (plugin.quiz == null || num > plugin.quizConfig.maxNum) {
            plugin.getLogger().warning("Exercise为空，请检查文件存储！");
            return;
        }

        plugin.quiz.answeredPlayers = new HashSet<>();
        plugin.quiz.winner = null;
        SendText();
    }

    // 发送文本组件
    private void SendText() {
        // 创建可点击的文本组件
        TextComponent message = new TextComponent("============\n§6§l新一轮Quiz开始啦！\n【点击这里】§r§e打开Quiz界面！\n============");
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
        if (plugin.quiz.winner != null) {
            player.sendMessage(plugin.QuizConfig.getString("message.hasCorrect", "已有玩家回答正确！"));
            Zhisuan11core.main.gameMenu.closeMenu(player);
            return;
        }
        // 检查玩家是否已经回答过
        if (plugin.quiz.answeredPlayers.contains(playerId)) {
            player.sendMessage(plugin.QuizConfig.getString("message.hasAnswered", "您已经回答过此问题！"));
            Zhisuan11core.main.gameMenu.closeMenu(player);
            return;
        }
        // 记录玩家已回答
        plugin.quiz.answeredPlayers.add(playerId);
        // 回答正确
        if (response.equals(plugin.quiz.answer)) {
            // 给予奖励
            player.getInventory().addItem(plugin.quiz.reward);
            // 发送提示音效
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
            Zhisuan11core.main.gameMenu.closeMenu(player);
            plugin.quiz.winner = playerId;
            player.sendMessage(plugin.QuizConfig.getString("message.correct", "回答正确，奖励已发放！"));
            return;
        }
        // 回答错误
        player.sendMessage(plugin.QuizConfig.getString("message.wrong", "回答错误！"));
        Zhisuan11core.main.gameMenu.closeMenu(player);
    }

    /* ---- 问题生成 ----- */
    // 初始化Quiz
    public void initialQuiz() {
        // 生成Quiz.yml文件
        plugin.QuizFile = new File(plugin.getDataFolder(), "Quiz.yml");
        if (!plugin.QuizFile.exists()) {
            plugin.saveResource("Quiz.yml", false); // 假设你在jar包的根目录放了默认文件
        }
        plugin.QuizConfig = YamlConfiguration.loadConfiguration(plugin.QuizFile);

        plugin.quizConfig.setInterval();
        plugin.quizConfig.setStorageType();

        // 设置Quiz存储格式
        if (plugin.quizConfig.getStorageType().equalsIgnoreCase("YAML")) {
            plugin.quizConfig.QuizMap = plugin.QuizConfig.getMapList("List");
            // 重置QuizList
            plugin.quizConfig.QuizList = new ArrayList<>();
            SetQuizForYaml();
            plugin.quizConfig.maxNum = plugin.quizConfig.QuizList.size();
            plugin.getLogger().info("Quiz使用YAML格式");
        } else if (plugin.quizConfig.getStorageType().equalsIgnoreCase("MYSQL")) {
            // 设置数据库信息
            plugin.databaseConfig.setType(plugin.QuizConfig.getString("config.storage", "mysql"));
            plugin.databaseConfig.setHost(plugin.QuizConfig.getString("config.host", "localhost"));
            plugin.databaseConfig.setPort(plugin.QuizConfig.getInt("config.port", 3306));
            plugin.databaseConfig.setDatabase(plugin.QuizConfig.getString("config.database", "zhisuan11"));
            plugin.databaseConfig.setUsername(plugin.QuizConfig.getString("config.name", "root"));
            plugin.databaseConfig.setPassword(plugin.QuizConfig.getString("config.password", ""));
            plugin.databaseConfig.setUseSSL(plugin.QuizConfig.getBoolean("config.useSSL", false));

            plugin.dataSourceManager = new DataSourceManager(plugin.databaseConfig);
            DatabaseInitial databaseInitial = new DatabaseInitial(plugin.dataSourceManager);
            databaseInitial.InitialTables();

            plugin.quizForSql = new QuizForSql(plugin.dataSourceManager);
            plugin.quizForSql.setQuiz();

            plugin.quizConfig.maxNum = plugin.quizForSql.maxId;

            plugin.getLogger().info("Quiz使用MYSQL格式");
        }
    }

    // 设置问答池
    public void SetQuizForYaml() {
        if (plugin.quizConfig.QuizMap == null || plugin.quizConfig.QuizMap.isEmpty()) {
            Zhisuan11core.main.getLogger().warning("问答池为空！");
            return;
        }

        for (Map<?, ?> taskMap : plugin.quizConfig.QuizMap) {
            Quiz temp = new Quiz();

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

            plugin.quizConfig.QuizList.add(temp);
        }

        plugin.getLogger().info("当前题库共有" + plugin.quizConfig.QuizList.size() + "题");
    }
}
