package org.zhisuan11.zhisuan11core;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class Zhisuan11TabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("zhisuan11") || command.getName().equalsIgnoreCase("zs")) {
            if (args.length == 1) {
                // 提供参数补全建议
                List<String> completions = new ArrayList<>();   //创建参数补全列表
                completions.add("help");
                completions.add("info");
                completions.add("broadcast");
                completions.add("reload");
                completions.add("setspawn");
                completions.add("kit");
                return completions;
            }

            else if (args.length == 2 && args[0].equals("broadcast")) {
                List<String> completions = new ArrayList<>();   //创建参数补全列表
                completions.add("send");
                return completions;
            }
        }

        if (command.getName().equalsIgnoreCase("tp")) {
            if (args.length == 1) {
                List<String> completions = new ArrayList<>();
                completions.add("@s");
                completions.add("@p");
                return completions;
            }

            else if (args.length == 2) {
                List<String> completions = new ArrayList<>();
                completions.add("spawn");
                return completions;
            }
        }

        return null;
    }
}
