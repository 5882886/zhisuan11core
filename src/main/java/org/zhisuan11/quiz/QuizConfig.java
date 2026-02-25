package org.zhisuan11.quiz;

import org.zhisuan11.Zhisuan11core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Quiz的相关参数
public class QuizConfig {

    Zhisuan11core plugin = Zhisuan11core.main;

    // 配置参数
    private int interval;
    private String storageType;
    public int maxNum;

    public List<Map<?, ?>> QuizMap = new ArrayList<>();
    public List<Quiz> QuizList = new ArrayList<>();

    public QuizConfig() {
        this.interval = 900;
        this.storageType = "YAML";
        this.maxNum = 0;
    }

    public int getInterval() {return interval;}

    public void setInterval() {interval = plugin.QuizConfig.getInt("config.interval", 900);}

    public String getStorageType() {return storageType;}

    public void setStorageType() {storageType = plugin.QuizConfig.getString("config.storage", "YAML");}

    public int getMaxNum() {return maxNum;}
}
