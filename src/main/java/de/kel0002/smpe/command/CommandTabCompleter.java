package de.kel0002.smpe.command;

import de.kel0002.smpe.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1){
            if (sender.hasPermission("events.manage")) {
                suggestions.add("start");
                suggestions.add("stop");
            }
            if (!(sender instanceof Player player)) return suggestions; //Unnecessary, I believe, but not sure
            if (Main.getEventManager().is_in_any(player)) {
                if (sender.hasPermission("events.voteskip")) {
                    suggestions.add("voteskip");
                }
                suggestions.add("leave");
            }
            suggestions.add("list");
        }
        if (args.length == 2) {
            if ( "stop".equalsIgnoreCase(args[0]) && sender.hasPermission("events.manage")) {
                suggestions.addAll(Main.getEventManager().all_event_ints());
                suggestions.add("all");
            }
        }

        return suggestions;
    }
}
