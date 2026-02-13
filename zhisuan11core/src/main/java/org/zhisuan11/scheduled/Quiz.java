package org.zhisuan11.scheduled;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.zhisuan11.Zhisuan11core;
import org.zhisuan11.gui.GameMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class Quiz extends BukkitRunnable {

    @Override
    public void run() {
        SendQuiz();
    }

    // 题目类
    public static class Task {
        public String question;
        public List<String> options;
        public String answer;
    }

    private final Zhisuan11core plugin = Zhisuan11core.main;

    // 间隔时间
    int interval = Zhisuan11core.main.getConfig().getInt("Quiz.interval", 900);

    // 设置问答池
    public void setQuiz() {
        if (plugin.taskMap == null || plugin.taskMap.isEmpty()) {
            Zhisuan11core.main.getLogger().warning("问答池为空！");
            return;
        }

        for (Map<?, ?> taskMap : plugin.taskMap) {
            Task temp = new Task();

            temp.question = (String) taskMap.get("Question");
            temp.answer = (String) taskMap.get("Answer");

            Map<?, ?> options = (Map<?, ?>) taskMap.get("Options");
            temp.options = new ArrayList<>();
            temp.options.add((String) options.get("A"));
            temp.options.add((String) options.get("B"));
            temp.options.add((String) options.get("C"));
            temp.options.add((String) options.get("D"));

            plugin.taskList.add(temp);
        }
    }

    // 选择问题
    public Task chooseQuiz() {
        Random random = new Random();
        return plugin.taskList.get(random.nextInt(plugin.taskList.size()));
    }


    // 问题回答界面在 /gui/GameMenu.java
    // 发送随机问题
    public void SendQuiz() {
        setQuiz();
        plugin.task = chooseQuiz();
        // 创建可点击的文本组件
        TextComponent message = new TextComponent("§6§l新一轮Quiz开始啦！\n【点击这里】§r§e打开Quiz界面！");
        // 点击事件：玩家点击后会自动输入并执行此命令
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zs menu quiz"));
        // 悬浮文本
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7点击参加Quiz！")));
        Zhisuan11core.main.getServer().spigot().broadcast(message);
    }

    // 发送特定问题
    public void SendSpecificQuiz(int num) {
        setQuiz();
        plugin.task = plugin.taskList.get(num);

        TextComponent message = new TextComponent("§6§l【点击这里】§r§e打开Quiz界面！");
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zs menu quiz"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§7点击参加Quiz！")));

        Zhisuan11core.main.getServer().spigot().broadcast(message);
    }

    // 检查是否回答正确
    public void check(Player player) {
        if (plugin.response.equals(plugin.task.answer)) {
            player.sendMessage("回答正确！");
            return;
        }
        player.sendMessage("回答错误！");
        GameMenu.closeMenu(player);
    }
}
