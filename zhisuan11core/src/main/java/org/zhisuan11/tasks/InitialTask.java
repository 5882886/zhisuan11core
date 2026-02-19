package org.zhisuan11.tasks;

import org.zhisuan11.Zhisuan11core;

// 启动插件仅执行一次的内容
public class InitialTask {

    private final Zhisuan11core plugin = Zhisuan11core.main;

    public void initialTasks() {
        plugin.quiz.initialQuiz();
    }

}
