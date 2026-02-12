package org.zhisuan11.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("zhisuan11") || command.getName().equalsIgnoreCase("zs")) {
            if (args.length == 1) {
                // 提供参数补全建议
                List<String> completions = new ArrayList<>();
                //创建参数补全列表
                AddCompletions(completions, "help", "info", "menu", "kit", "broadcast", "setspawn", "reload");
                return completions;
            } else if (args.length == 2) {
                if (args[0].equals("broadcast")) {
                    List<String> completions = new ArrayList<>();
                    AddCompletions(completions, "send");
                    return completions;
                } else if (args[0].equals("menu")) {
                    List<String> completions = new ArrayList<>();
                    AddCompletions(completions, "main", "quiz");
                    return completions;
                }
            }
        }

        if (command.getName().equalsIgnoreCase("tp")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                AddCompletions(completions, "@s", "@p");
                return completions;
            } else if (args.length == 2) {
                List<String> completions = new ArrayList<>();
                AddCompletions(completions, "spawn");
                return completions;
            }
        }

        return null;
    }

    private void AddCompletions(List<String> completions, String... strings) {
        if (strings != null && strings.length > 0) {
            completions.addAll(Arrays.asList(strings));
        }
    }
}
