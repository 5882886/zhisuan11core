package org.zhisuan11.tasks;

import org.zhisuan11.Zhisuan11core;

// 启动插件定时执行的内容
public class SeverTask {

    private final Zhisuan11core plugin = Zhisuan11core.main;

    // 初始化任务
    public void InitialTasks() {
        // 初始化问答列表
        plugin.quiz.initialQuiz();
        // 掉落物清理
        plugin.clearDropItems.loadConfig();
    }

    // 定时任务
    public void ScheduleTasks() {
        // 游戏内侧边栏
        plugin.sidebar.startUpdate();
        // 发送Quiz
        plugin.quiz.SendTask();
        // 清理掉落物
        plugin.clearDropItems.startClear();
        // 服务器公告
        plugin.broadcast.SendTask();
        // 检查玩家延迟
        plugin.checkPing.SendTask();
    }

    // 结束任务
    public void CloseTasks() {
        // 关闭数据库连接
        if (plugin.quizForSql != null) {
            plugin.quizForSql.close();
        }
    }
}
