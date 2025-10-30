package de.kel0002.smpEvents.PlayerLevel;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Progress {
	public static int getServerProgress(){

        if (Bukkit.getOnlinePlayers().isEmpty()) return 0;

		List<Integer> progresses = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()){
			progresses.add(getPlayerProgress(player));
		}
		int worst_progress = Collections.min(progresses);
		progresses.add(worst_progress);
		progresses.add(worst_progress);


		return (int) progresses.stream()
				.mapToInt(Integer::intValue)
				.average()
				.orElse(0);
	}


	public static int getPlayerProgress(Player player){
		double totalPoints = 0;
		double armorPoints = 0;
		for (ItemStack armor : player.getInventory().getArmorContents()) {
			armorPoints += getItemValue(armor);
		}

		totalPoints += armorPoints/2 - 10;
		if (player.getInventory().contains(Material.TOTEM_OF_UNDYING) ||
				player.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) totalPoints += 10;

		return Math.min(Math.max((int) Math.round(totalPoints), 0), 100);
	}



	public static int getItemValue(ItemStack armor){
		if (armor == null) return 0;
		return switch (armor.getType()) {
			case LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS -> 5;
			case GOLDEN_HELMET, GOLDEN_CHESTPLATE, GOLDEN_LEGGINGS, GOLDEN_BOOTS, CHAINMAIL_HELMET,
			     CHAINMAIL_CHESTPLATE, CHAINMAIL_LEGGINGS, CHAINMAIL_BOOTS -> 10;
			case IRON_HELMET, IRON_CHESTPLATE, IRON_LEGGINGS, IRON_BOOTS -> 20;
			case DIAMOND_HELMET, DIAMOND_CHESTPLATE, DIAMOND_LEGGINGS, DIAMOND_BOOTS -> 40;
			case NETHERITE_HELMET, NETHERITE_CHESTPLATE, NETHERITE_LEGGINGS, NETHERITE_BOOTS, ELYTRA -> 50;
			default -> 0;
		};
	}

}
