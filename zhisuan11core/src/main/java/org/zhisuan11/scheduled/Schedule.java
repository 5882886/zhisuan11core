package org.zhisuan11.scheduled;

import org.bukkit.plugin.Plugin;
import org.zhisuan11.core.Sidebar;
import org.zhisuan11.Zhisuan11core;

public class Schedule {

    Plugin plugin = Zhisuan11core.getInstance();

    public void ScheduleTasks() {
        // 游戏内侧边栏
        Sidebar updater = new Sidebar();
        updater.runTaskTimer(plugin, 0L, 20L);

        // 服务器公告
        Broadcast broadcast = new Broadcast();
        broadcast.runTaskTimer(plugin, 20L * 15, 20L * broadcast.interval);

        // 发送Quiz
        Quiz quiz = new Quiz();
        quiz.runTaskTimer(plugin, 20L * 15, 20L * quiz.interval);
    }
}
