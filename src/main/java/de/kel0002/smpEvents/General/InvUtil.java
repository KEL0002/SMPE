package de.kel0002.smpEvents.General;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Objects;

public class InvUtil {
	public static boolean contains(Player player, Material material, int amount){
		return player.getInventory().contains(material, amount);
	}

	public static boolean remove(Player player, Material material, int amount){
		if (!contains(player,material, amount)) return false;
		for (int i = 0; i < amount; i++) {
			player.getInventory().removeItemAnySlot(new ItemStack(material, 1));
		}
		return true;
	}

	public static void give(Player player, Material material, int amount){
		for (int i = 0; i < amount; i++){
			if (!isFull(player, material)){ player.getInventory().addItem(new ItemStack(material, 1));}
			else {player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(material, 1));}
		}
	}

	public static boolean isFull(Player player){
		return Arrays.stream(player.getInventory().getStorageContents()).allMatch(Objects::nonNull);
	}
	public static boolean isFull(Player player, Material material){
		return Arrays.stream(player.getInventory().getStorageContents())
				.noneMatch(item -> item == null || (item.getType() == material && item.getAmount() < material.getMaxStackSize()));
	}
}
