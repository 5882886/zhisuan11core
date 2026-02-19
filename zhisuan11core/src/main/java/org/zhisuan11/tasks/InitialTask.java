package org.zhisuan11.tasks;

import org.bukkit.configuration.file.YamlConfiguration;
import org.zhisuan11.Zhisuan11core;
import org.zhisuan11.quiz.QuizForYaml;
import org.zhisuan11.quiz.QuizForSql;

import java.io.File;

// 启动插件仅执行一次的内容
public class InitialTask {
    private final Zhisuan11core plugin = Zhisuan11core.main;

    public void initialTasks() {

        plugin.quiz.initialQuiz();
    }

}
