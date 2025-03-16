package de.kel0002.smpEvents.Commands;

import de.kel0002.smpEvents.Main;
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
			if (sender.hasPermission("events.manage")){
				suggestions.add("start");
				suggestions.add("stop");
			}
			if (Main.getEventManager().getCurrentEvent() != null){
				if (sender.hasPermission("events.join") && Main.getEventManager().getCurrentEvent().hasPreStarted && !Main.getEventManager().eventInMainGame()){
					suggestions.add("join");
				}
				if (Main.getEventManager().eventRunning()){
					if (sender.hasPermission("events.voteskip") && Main.getEventManager().eventInMainGame() && Main.getEventManager().getCurrentEvent().joined_players.contains((Player) sender)){
						suggestions.add("voteskip");
					}
					if (Main.getEventManager().eventInMainGame() && Main.getEventManager().getCurrentEvent().joined_players.contains((Player) sender)){
						suggestions.add("leave");
					}
				}
			}
		}
		return suggestions;
	}
}
