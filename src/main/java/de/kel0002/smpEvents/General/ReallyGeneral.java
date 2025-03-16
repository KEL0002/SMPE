package de.kel0002.smpEvents.General;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class ReallyGeneral {
	public static World getWorld(World.Environment environment){
		for (World world : Bukkit.getWorlds()){
			if (world.getEnvironment() == environment) {
				return world;
			}
		}
		return Bukkit.getWorld("world");
	}

	public static void playSound(List<Player> playerList, Sound sound){
		for (Player player : playerList){
			playSound(player, sound);
		}
	}

	public static void playSound(Player player, Sound sound){
		player.getWorld().playSound(player.getLocation(), sound, 1.0f, 1.0f);
	}
	public static void playSound(Sound sound){
		for (Player player : Bukkit.getOnlinePlayers()){
			playSound(player, sound);
		}
	}
}
