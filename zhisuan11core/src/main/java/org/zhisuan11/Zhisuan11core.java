package org.zhisuan11;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import org.zhisuan11.command.MainCommand;
import org.zhisuan11.command.TeleportCommand;
import org.zhisuan11.command.CommandTabCompleter;
import org.zhisuan11.core.JoinInfo;
import org.zhisuan11.core.JoinItem;
import org.zhisuan11.core.Respawn;
import org.zhisuan11.gui.GameMenuClick;
import org.zhisuan11.scheduled.Quiz;
import org.zhisuan11.scheduled.Schedule;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public final class Zhisuan11core extends JavaPlugin {

    // 全局变量代表主类
    public static Zhisuan11core main;

    public static Zhisuan11core getInstance() {
        return main;
    }

    public FileConfiguration QuizConfig;

    // 问答类全局变量
    public List<Map<?, ?>> taskMap;
    public List<Quiz.Task> taskList = new ArrayList<>();
    public Quiz.Task task;
    public String response;

    @Override
    public void onEnable() {

        main = this;

        File QuizFile = new File(getDataFolder(), "Quiz.yml");
        if (!QuizFile.exists()) {
            saveResource("Quiz.yml", false); // 假设你在jar包的根目录放了默认文件
        }
        QuizConfig = YamlConfiguration.loadConfiguration(QuizFile);
        taskMap = QuizConfig.getMapList("List");

        // Plugin startup logic
        getLogger().info("欢迎您使用智算11班服务器插件！");
        getLogger().info("插件仓库：https://github.com/5882886/My-Minecraft-Server！");
        getLogger().info(" ");
        getLogger().info("███████╗██╗░░██╗██╗░██████╗██╗░░░██╗░█████╗░███╗░░██╗░░███╗░░░░███╗░░");
        getLogger().info("╚════██║██║░░██║██║██╔════╝██║░░░██║██╔══██╗████╗░██║░████║░░░████║░░");
        getLogger().info("░░███╔═╝███████║██║╚█████╗░██║░░░██║███████║██╔██╗██║██╔██║░░██╔██║░░");
        getLogger().info("██╔══╝░░██╔══██║██║░╚═══██╗██║░░░██║██╔══██║██║╚████║╚═╝██║░░╚═╝██║░░");
        getLogger().info("███████╗██║░░██║██║██████╔╝╚██████╔╝██║░░██║██║░╚███║███████╗███████╗");
        getLogger().info("╚══════╝╚═╝░░╚═╝╚═╝╚═════╝░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚══╝╚══════╝╚══════╝");
        getLogger().info(" ");


        Objects.requireNonNull(Bukkit.getPluginCommand("zhisuan11")).setExecutor(new MainCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("tp")).setExecutor(new TeleportCommand());

        Objects.requireNonNull(Bukkit.getPluginCommand("zhisuan11")).setTabCompleter(new CommandTabCompleter());

        // 注册服务器事件
        Bukkit.getPluginManager().registerEvents(new JoinInfo(), this);
        Bukkit.getPluginManager().registerEvents(new JoinItem(), this);
        Bukkit.getPluginManager().registerEvents(new Respawn(), this);
        Bukkit.getPluginManager().registerEvents(new GameMenuClick(), this);

        // 生成配置文件
        saveDefaultConfig();
        reloadConfig();

        // 服务器定时任务
        Schedule schedule = new Schedule();
        schedule.ScheduleTasks();
    }

    @Override
    public void onDisable() {

        // Plugin shutdown logic
        getLogger().info("感谢您使用智算11班服务器插件！");
        getLogger().info("插件仓库：https://github.com/5882886/My-Minecraft-Server！");
        getLogger().info(" ");
        getLogger().info("███████╗██╗░░██╗██╗░██████╗██╗░░░██╗░█████╗░███╗░░██╗░░███╗░░░░███╗░░");
        getLogger().info("╚════██║██║░░██║██║██╔════╝██║░░░██║██╔══██╗████╗░██║░████║░░░████║░░");
        getLogger().info("░░███╔═╝███████║██║╚█████╗░██║░░░██║███████║██╔██╗██║██╔██║░░██╔██║░░");
        getLogger().info("██╔══╝░░██╔══██║██║░╚═══██╗██║░░░██║██╔══██║██║╚████║╚═╝██║░░╚═╝██║░░");
        getLogger().info("███████╗██║░░██║██║██████╔╝╚██████╔╝██║░░██║██║░╚███║███████╗███████╗");
        getLogger().info("╚══════╝╚═╝░░╚═╝╚═╝╚═════╝░░╚═════╝░╚═╝░░╚═╝╚═╝░░╚══╝╚══════╝╚══════╝");
        getLogger().info(" ");

    }
}
