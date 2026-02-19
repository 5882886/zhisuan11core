package org.zhisuan11.quiz;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.zhisuan11.Zhisuan11core;

import java.util.*;

// YAML存储格式的Quiz
public class QuizForYaml {

    private final Zhisuan11core plugin = Zhisuan11core.main;

    // 设置问答池
    public void setQuiz() {
        if (plugin.taskMap == null || plugin.taskMap.isEmpty()) {
            Zhisuan11core.main.getLogger().warning("问答池为空！");
            return;
        }

        for (Map<?, ?> taskMap : plugin.taskMap) {
            Quiz.Task temp = new Quiz.Task();

            temp.question = (String) taskMap.get("Question");
            temp.answer = (String) taskMap.get("Answer");

            Map<?, ?> options = (Map<?, ?>) taskMap.get("Options");
            temp.options = new ArrayList<>();
            temp.options.add((String) options.get("A"));
            temp.options.add((String) options.get("B"));
            temp.options.add((String) options.get("C"));
            temp.options.add((String) options.get("D"));

            // 设置奖品
            Object rewardObj = taskMap.get("Reward");
            String rewardName = rewardObj.toString();
            Material rewardMaterial = Material.matchMaterial(rewardName.toUpperCase());
            temp.reward = new ItemStack(Objects.requireNonNull(rewardMaterial));

            plugin.taskList.add(temp);
        }
    }
}
