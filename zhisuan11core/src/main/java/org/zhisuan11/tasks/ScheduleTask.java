package org.zhisuan11.tasks;

import org.zhisuan11.Zhisuan11core;

// 启动插件定时执行的内容
public class ScheduleTask {

    private final Zhisuan11core plugin = Zhisuan11core.main;

    public void ScheduleTasks() {
        // 游戏内侧边栏
        plugin.sidebar.runTaskTimer(plugin, 0L, 20L);
        // 服务器公告
        plugin.broadcast.runTaskTimer(plugin, 20L * 15, 20L * plugin.broadcast.interval);
        // 发送Quiz
        plugin.quiz.runTaskTimer(plugin, 20L * 15, 20L * plugin.quiz.interval);
    }
}
