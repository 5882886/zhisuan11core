package org.zhisuan11.tasks;

import org.zhisuan11.Zhisuan11core;

// 启动插件仅执行一次的内容
public class InitialTask {

    private final Zhisuan11core plugin = Zhisuan11core.main;

    public void initialTasks() {
        // 初始化问答列表
        plugin.quiz.initialQuiz();
        // 掉落物清理
        plugin.clearDropItems.loadConfig();
    }

}
