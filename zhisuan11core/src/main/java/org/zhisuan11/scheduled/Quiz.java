package org.zhisuan11.scheduled;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.scheduler.BukkitRunnable;
import org.zhisuan11.core.Zhisuan11core;


public class Quiz extends BukkitRunnable {

    int interval = Zhisuan11core.main.getConfig().getInt("Quiz.interval", 900);

    @Override
    public void run() {
        SendQuiz();
    }

    public static void SendQuiz() {
        // 创建可点击的文本组件
        TextComponent message = new TextComponent("§6§l【点击这里】§r§e打开Quiz界面！");

        // 点击事件
        message.setClickEvent(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/zs menu quiz"  // 玩家点击后会自动输入并执行此命令
        ));
        // 悬浮文本
        message.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new Text("§7点击参加Quiz！")
        ));

        Zhisuan11core.main.getServer().spigot().broadcast(message);
    }
}
