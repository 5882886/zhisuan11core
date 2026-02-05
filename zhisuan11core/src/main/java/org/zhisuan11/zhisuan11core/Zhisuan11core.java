package org.zhisuan11.zhisuan11core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;


public final class Zhisuan11core extends JavaPlugin {

    //全局变量代表主类
    static Zhisuan11core main;

    @Override
    public void onEnable() {

        main = this;

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


        Objects.requireNonNull(Bukkit.getPluginCommand("zhisuan11")).setExecutor(new Zhisuan11CommandMain());
        Objects.requireNonNull(Bukkit.getPluginCommand("tp")).setExecutor(new Zhisuan11CommandTeleport());

        Objects.requireNonNull(Bukkit.getPluginCommand("zhisuan11")).setTabCompleter(new Zhisuan11TabCompleter());

        // 注册服务器事件
        Bukkit.getPluginManager().registerEvents(new JoinInfo(), this);
        Bukkit.getPluginManager().registerEvents(new JoinItem(), this);
        Bukkit.getPluginManager().registerEvents(new Respawn(), this);
        Bukkit.getPluginManager().registerEvents(new GameMenuClick(), this);

        // 生成配置文件
        saveDefaultConfig();
        reloadConfig();

        SidebarUpdate updater = new SidebarUpdate();
        updater.runTaskTimer(this, 0L, 20L);

        // 实现定时循环发送服务器公告
        List<String> announcement = getConfig().getStringList("BroadCast.content");
        String bool = getConfig().getString("BroadCast.enabled");
        int interval = getConfig().getInt("BroadCast.interval");

        if (bool != null && bool.equals("true")) {
            getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
                for (String message : announcement) {
                    message = ChatColor.translateAlternateColorCodes('&', message);
                    getServer().broadcastMessage(message);
                }
            }, 20L * 15, 20L * interval);   // 加载插件15秒后发送第一次公告，此后每interval秒发送一次
        }

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
